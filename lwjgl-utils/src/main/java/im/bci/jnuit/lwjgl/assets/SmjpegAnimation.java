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
package im.bci.jnuit.lwjgl.assets;

import im.bci.jnuit.animation.IAnimation;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IAnimationImage;
import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.smjpegdecoder.SmjpegDecoder;
import im.bci.smjpegdecoder.SmjpegOutputBuffers;
import java.io.IOException;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class SmjpegAnimation implements IAnimationCollection, IAnimation {

    private final SmjpegDecoder decoder;

    SmjpegAnimation(SmjpegDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public IAnimation getAnimationByName(String name) {
        return this;
    }

    @Override
    public IAnimation getFirst() {
        return this;
    }

    @Override
    public String getName() {
        return "smjpeg";
    }

    @Override
    public IPlay start(PlayMode mode) {
        SmjpegPlay play = new SmjpegPlay();
        play.start(mode);
        return play;
    }

    @Override
    public void stop(IPlay play) {
        play.stop();
    }

    enum State {

        STARTED, STOPPED
    }

    private class SmjpegAnimationFrame implements IAnimationFrame, IAnimationImage {

        private final Texture texture;
        private final SmjpegOutputBuffers outputBuffers;

        SmjpegAnimationFrame() {
            try {
                texture = createTexture();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
                LwjglHelper.setupGLTextureParams();
                outputBuffers = new SmjpegOutputBuffers(decoder);
                decoder.getFrame(0, outputBuffers);
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                LwjglHelper.setupGLTextureParams();
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, decoder.getVideoWidth(), decoder.getVideoHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, outputBuffers.getVideoFrame());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        private void updateTexture(int currentTime) {
            try {
                decoder.getFrame(currentTime, outputBuffers);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, decoder.getVideoWidth(), decoder.getVideoHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, outputBuffers.getVideoFrame());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public long getDuration() {
            return 1;//arbitrary...
        }

        @Override
        public IAnimationImage getImage() {
            return this;
        }

        @Override
        public float getU1() {
            return 0f;
        }

        @Override
        public float getU2() {
            return 1f;
        }

        @Override
        public float getV1() {
            return 0f;
        }

        @Override
        public float getV2() {
            return 1f;
        }

        @Override
        public int getId() {
            return texture.getId();
        }

        @Override
        public boolean hasAlpha() {
            return texture.hasAlpha();
        }

    }

    private class SmjpegPlay implements IPlay {

        private PlayMode mode = PlayMode.LOOP;
        private State state = State.STOPPED;
        private int currentTime;
        private final SmjpegAnimationFrame frame = new SmjpegAnimationFrame();

        @Override
        public String getName() {
            return SmjpegAnimation.this.getName();
        }

        @Override
        public void start(PlayMode mode) {
            this.mode = mode;
            state = State.STARTED;
            currentTime = 0;
        }

        @Override
        public void stop() {
            state = State.STOPPED;
        }

        @Override
        public boolean isStopped() {
            return state == State.STOPPED;
        }

        @Override
        public PlayMode getMode() {
            return mode;
        }

        @Override
        public void update(long elapsedTime) {
            currentTime += elapsedTime;
            if (currentTime > decoder.getClipLengthInMilliseconds()) {
                switch (mode) {
                    case LOOP:
                        currentTime = 0;
                        break;
                    case ONCE:
                        state = State.STOPPED;
                        break;
                }
            }
            if (!isStopped()) {
                frame.updateTexture(currentTime);
            }
        }

        @Override
        public IAnimationFrame getCurrentFrame() {
            return frame;
        }

        @Override
        public void restart() {
            stop();
            start(mode);
        }
    }

    protected Texture createTexture() {
        return new Texture(decoder.getVideoWidth(), decoder.getVideoHeight(), false);
    }

}
