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
package im.bci.jnuit.lwjgl.smjpeg;

import de.matthiasmann.jpegdecoder.JPEGDecoder;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 *
 * @author devnewton
 */
public class SmjpegParser {

    private final RandomAccessFile file;
    private int clipLengthInMilliseconds;
    private int audioRate;
    private int audioBitsPerSample;
    private int audioChannels;
    private SmjpegAudioEncoding audioEncoding;
    private int videoWidth;
    private int videoHeight;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String testDir = System.getProperty("user.home")+ "/tmp/smjpeg";
        try (RandomAccessFile file = new RandomAccessFile(testDir + "/test.smjpeg", "r")) {
            SmjpegParser parser = new SmjpegParser(file);
            final int timePerFrame = 1000 / 60;
            int timeElapsed = 0;
            BufferedImage image = new BufferedImage(parser.getVideoWidth(), parser.getVideoHeight(), BufferedImage.TYPE_INT_RGB);
            int frame = 0;
            SmjpegOutputBuffers outputBuffers = new SmjpegOutputBuffers(parser);
            while (parser.readSome(timeElapsed, outputBuffers)) {
                int index = 0;
                for (int y = 0; y < parser.getVideoHeight(); ++y) {
                    for (int x = 0; x < parser.getVideoWidth(); ++x) {
                        int r = outputBuffers.getVideoFrame().get(index++) & 0xff;
                        int g = outputBuffers.getVideoFrame().get(index++) & 0xff;
                        int b = outputBuffers.getVideoFrame().get(index++) & 0xff;
                        int a = outputBuffers.getVideoFrame().get(index++) & 0xff;
                        Color c = new Color(r, g, b, a);
                        image.setRGB(x, y, c.getRGB());
                    }
                }
                ImageIO.write(image, "png", new File(testDir + "/frames/" + (frame++) + ".png"));
                timeElapsed += timePerFrame;
            }
        }
    }

    private SmjpegParser(RandomAccessFile file) throws IOException {
        this.file = file;
        decodeMandatoryHeader();
        decodeOptionalHeaders();
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

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public int getClipLengthInMilliseconds() {
        return clipLengthInMilliseconds;
    }

    /*
     Interleaved chunks of audio/video data:
     4 bytes magic - "sndD" for sound data, "vidD" for video data
     Uint32 millisecond timestamp
     Uint32 chunk length 
     */
    public boolean readSome(int currentTimeInMilliseconds, SmjpegOutputBuffers outputBuffers) throws IOException {
        for (;;) {
            long fp = file.getFilePointer();
            if (fp >= file.length()) {
                return false;
            }
            byte[] magicBytes = new byte[4];
            file.readFully(magicBytes);
            String magic = new String(magicBytes, "UTF-8");
            if ("DONE".equals(magic)) {
                file.seek(file.length());
                return false;
            }

            int timestamp = file.readInt();
            if (timestamp > currentTimeInMilliseconds) {
                file.seek(fp);
                return true;
            }
            int length = file.readInt();
            fp = file.getFilePointer();
            switch (magic) {
                case "sndD":
                    decodeAudioChunk();
                    break;
                case "vidD":
                    decodeVideoChunk(outputBuffers);
                    break;
            }
            file.seek(fp + length);
        }
    }

    private static final byte[] mandatoryHeaderMagic = new byte[]{0, '\n', 'S', 'M', 'J', 'P', 'E', 'G'};

    /*
     8 bytes magic - "^@\nSMJPEG"
     Uint32 version = 0
     Uint32 length of clip in milliseconds
     */
    private void decodeMandatoryHeader() throws IOException {
        byte[] magicBytes = new byte[8];
        file.readFully(magicBytes);
        if (!Arrays.equals(mandatoryHeaderMagic, magicBytes)) {
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
            String magic = new String(magicBytes, "UTF-8");
            switch (magic) {
                case "_TXT":
                    decodeCommentHeader();
                    break;
                case "_SND":
                    decodeAudioHeader();
                    break;
                case "_VID":
                    decodeVideoHeader();
                    break;
                case "HEND":
                    return;
                default:
                    throw new SmjpegParsingException("Unknow SMJPEG header: " + magic);
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
        String audioEncodingStr = new String(audioEncodingBytes, "UTF-8");
        audioEncoding = SmjpegAudioEncoding.valueOf(audioEncodingStr);

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
        /*int videoFrameCount = always zero with ffmpeg encoded files...*/
        file.readInt();
        videoWidth = file.readUnsignedShort();
        videoHeight = file.readUnsignedShort();
        byte[] videoEncodingBytes = new byte[4];
        file.readFully(videoEncodingBytes);
        String videoEncodingStr = new String(videoEncodingBytes, "UTF-8");
        if (!"JFIF".equals(videoEncodingStr)) {
            throw new SmjpegParsingException("Unknow video encoding: " + videoEncodingStr);
        }
        file.skipBytes(Math.max(0, length - 4 - 4 - 2 - 2));
    }

    private void decodeAudioChunk() {
//TODO
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
