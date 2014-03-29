/*
 The MIT License (MIT)

 Copyright (c) 2011 Asier <info@infinityk.com>

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
package com.infinityk.simplesoundengine;

import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.lwjgl.openal.AL10;

/**
 *
 * @author Asier
 */
class SoundData {

    private static final Logger logger = Logger.getLogger(SoundData.class.getName());
    public final ByteBuffer data;
    public final int format;
    public final int samplerate;

    public SoundData(ByteBuffer data, int format, int samplerate) {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }

    public static SoundData create(String path, VirtualFileSystem vfs) {
        if (path.endsWith(".wav")) {
            return createFromWav(path, vfs);
        } else if (path.endsWith(".ogg")) {
            return createFromOgg(path, vfs);
        }
        return null;
    }

    private static SoundData createFromWav(String path, VirtualFileSystem gameData) {
        try {
            InputStream fis = gameData.open(path);
            try {
                BufferedInputStream bis = new BufferedInputStream(fis);
                AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
                AudioFormat info = ais.getFormat();
                int size = info.getChannels()
                        * (int) ais.getFrameLength()
                        * info.getSampleSizeInBits() / 8;
                byte[] array = new byte[size];
                ais.read(array);
                ByteBuffer buffer = convertAudioBytes(array, info.getSampleSizeInBits() == 16);
                return new SoundData(buffer, getALFormat(info), (int) info.getSampleRate());
            } finally {
                fis.close();
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Loading wav file " + path, ex);
        }
        return null;
    }

    private static SoundData createFromOgg(String path, VirtualFileSystem vfs) {

        try {
            InputStream fis = vfs.open(path);
            try {
                BufferedInputStream bis = new BufferedInputStream(fis);
                OggInputStream ogg = new OggInputStream(bis);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(128 * 1024);
                byte[] buffer = new byte[1024];
                int readed;
                while ((readed = ogg.read(buffer)) > 0) {
                    bos.write(buffer, 0, readed);
                }
                buffer = bos.toByteArray();
                ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length);
                bb.put(buffer);
                bb.rewind();
                return new SoundData(bb, getALFormat(ogg), ogg.getRate());
            } finally {
                fis.close();
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Loading ogg file " + path, ex);
        }
        return null;
    }

    private static ByteBuffer convertAudioBytes(byte[] audio, boolean is16bits) {
        ByteBuffer dest = ByteBuffer.allocateDirect(audio.length);
        dest.order(ByteOrder.nativeOrder());
        ByteBuffer src = ByteBuffer.wrap(audio);
        src.order(ByteOrder.LITTLE_ENDIAN);
        if (is16bits) {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining()) {
                dest_short.put(src_short.get());
            }
        } else {
            while (src.hasRemaining()) {
                dest.put(src.get());
            }
        }
        dest.rewind();
        return dest;
    }

    private static int getALFormat(AudioFormat info) {
        int format = 0;
        if (info.getChannels() == 1) {
            if (info.getSampleSizeInBits() == 8) {
                format = AL10.AL_FORMAT_MONO8;
            } else if (info.getSampleSizeInBits() == 16) {
                format = AL10.AL_FORMAT_MONO16;
            } else {
                throw new RuntimeException("Illegal sample size");
            }
        } else if (info.getChannels() == 2) {
            if (info.getSampleSizeInBits() == 8) {
                format = AL10.AL_FORMAT_STEREO8;
            } else if (info.getSampleSizeInBits() == 16) {
                format = AL10.AL_FORMAT_STEREO16;
            } else {
                throw new RuntimeException("Illegal sample size");
            }
        } else {
            throw new RuntimeException("Only mono or stereo is supported");
        }
        return format;
    }

    private static int getALFormat(OggInputStream ogg) {
        int format = 0;
        if (ogg.getChannels() == 1) {
            format = AL10.AL_FORMAT_MONO16;
        } else if (ogg.getChannels() == 2) {
            format = AL10.AL_FORMAT_STEREO16;
        } else {
            throw new RuntimeException("Only mono or stereo is supported");
        }
        return format;
    }
}
