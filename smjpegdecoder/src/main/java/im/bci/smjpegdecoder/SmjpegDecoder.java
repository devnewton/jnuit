/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package im.bci.smjpegdecoder;

import de.matthiasmann.jpegdecoder.JPEGDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 *
 * @author devnewton
 */
public class SmjpegDecoder {

    private final RandomAccessFile file;
    private int clipLengthInMilliseconds;
    private int audioRate;
    private int audioBitsPerSample;
    private int audioChannels;
    private SmjpegAudioEncoding audioEncoding;
    private SmjpegVideoEncoding videoEncoding;
    private int videoWidth;
    private int videoHeight;
    private final long dataFilePointer;

    public SmjpegDecoder(RandomAccessFile file) throws IOException {
        this.file = file;
        decodeMandatoryHeader();
        decodeOptionalHeaders();
        dataFilePointer = file.getFilePointer();
    }

    public int getAudioRate() {
        return audioRate;
    }

    public int getAudioBitsPerSample() {
        return audioBitsPerSample;
    }

    public int getAudioChannels() {
        return audioChannels;
    }

    public SmjpegAudioEncoding getAudioEncoding() {
        return audioEncoding;
    }

    public SmjpegVideoEncoding getVideoEncoding() {
        return videoEncoding;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public int getClipLengthInMilliseconds() {
        return clipLengthInMilliseconds;
    }

    public void getFrame(int timestamp, SmjpegOutputBuffers outputBuffers) throws IOException {
        file.seek(dataFilePointer);
        long lastVideoData = -1;
        int lastVideoTimestamp = -1;
        for (long fp = dataFilePointer; file.getFilePointer() < file.length(); fp = file.getFilePointer()) {
            byte[] magicBytes = new byte[4];
            file.readFully(magicBytes);
            if (Arrays.equals(SmjpegMagic.DONE_CHUNK, magicBytes)) {
                file.seek(file.length());
                break;
            }
            int chunkTimestamp = file.readInt();
            if (chunkTimestamp > timestamp) {
                break;
            }
            int length = file.readInt();
            if (Arrays.equals(SmjpegMagic.VIDEO_CHUNK, magicBytes)) {
                lastVideoData = file.getFilePointer();
                lastVideoTimestamp = chunkTimestamp;
            }
            file.skipBytes(length);
        }
        if (lastVideoData > 0) {
            if (lastVideoTimestamp != outputBuffers.getVideoFrameTimestamp()) {
                file.seek(lastVideoData);
                decodeVideoChunk(outputBuffers);
                outputBuffers.setVideoFrameTimestamp(lastVideoTimestamp);
            }
        }
    }

    /*
     8 bytes magic - "^@\nSMJPEG"
     Uint32 version = 0
     Uint32 length of clip in milliseconds
     */
    private void decodeMandatoryHeader() throws IOException {
        byte[] magicBytes = new byte[8];
        file.readFully(magicBytes);
        if (!Arrays.equals(SmjpegMagic.MANDATORY_HEADER, magicBytes)) {
            throw new SmjpegParsingException("This is not a SMJPEG file");
        }
        int version = file.readInt();
        if (version != 0) {
            throw new SmjpegParsingException("Unknow version " + version);
        }
        this.clipLengthInMilliseconds = file.readInt();
    }

    private void decodeOptionalHeaders() throws IOException {
        while (file.getFilePointer() < file.length()) {
            byte[] magicBytes = new byte[4];
            file.readFully(magicBytes);
            if (Arrays.equals(SmjpegMagic.COMMENT_HEADER, magicBytes)) {
                decodeCommentHeader();
            } else if (Arrays.equals(SmjpegMagic.AUDIO_HEADER, magicBytes)) {
                decodeAudioHeader();
            } else if (Arrays.equals(SmjpegMagic.VIDEO_HEADER, magicBytes)) {
                decodeVideoHeader();
            } else if (Arrays.equals(SmjpegMagic.END_HEADER, magicBytes)) {
                break;
            } else {
                throw new SmjpegParsingException("Unknow SMJPEG header: " + magicBytes);
            }
        }
    }

    /* One or more optional comment headers:
     4 bytes magic - "_TXT"
     Uint32 text length
     arbitrary text
     */
    private void decodeCommentHeader() throws IOException {
        int length = file.readInt();
        file.skipBytes(length);
    }

    /*Optional audio header:
     4 bytes magic - "_SND"
     Uint32 audio header length
     Uint16 audio rate
     Uint8  bits-per-sample  (8 = unsigned 8-bit audio, 16 = signed 16-bit LE audio)
     Uint8  channels         (1 = mono, 2 = stereo)
     4 bytes audio encoding  ("NONE" = none, "APCM" = ADPCM compressed)
     */
    private void decodeAudioHeader() throws IOException {
        int length = file.readInt();
        audioRate = file.readUnsignedShort();
        audioBitsPerSample = file.readUnsignedByte();
        audioChannels = file.readUnsignedByte();

        byte[] audioEncodingBytes = new byte[4];
        file.readFully(audioEncodingBytes);
        audioEncoding = SmjpegAudioEncoding.fromMagic(audioEncodingBytes);

        file.skipBytes(Math.max(0, length - 2 - 1 - 1 - 4));
    }

    /*
     Optional video header:
     4 bytes magic - "_VID"
     Uint32 video header length
     Uint32 number of frames
     Uint16 width
     Uint16 height
     4 bytes video encoding  ("JFIF" = jpeg)
     */
    private void decodeVideoHeader() throws IOException {
        int length = file.readInt();
        file.skipBytes(4);//video frame count is always zero with ffmpeg encoded files...
        videoWidth = file.readUnsignedShort();
        videoHeight = file.readUnsignedShort();
        byte[] videoEncodingBytes = new byte[4];
        file.readFully(videoEncodingBytes);
        videoEncoding = SmjpegVideoEncoding.fromMagic(videoEncodingBytes);
        file.skipBytes(Math.max(0, length - 4 - 4 - 2 - 2));
    }

    private void decodeVideoChunk(SmjpegOutputBuffers outputBuffers) throws IOException {
        JPEGDecoder jpeg = new JPEGDecoder(new FileInputStream(file.getFD()));
        jpeg.startDecode();
        final ByteBuffer videoFrame = outputBuffers.getVideoFrame();
        videoFrame.rewind();
        jpeg.decodeRGB(videoFrame, outputBuffers.getVideoFrameStride(), jpeg.getNumMCURows());
        videoFrame.rewind();
    }
}
