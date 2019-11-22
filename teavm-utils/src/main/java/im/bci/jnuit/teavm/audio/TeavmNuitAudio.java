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
package im.bci.jnuit.teavm.audio;

import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.audio.Sound;
import im.bci.jnuit.teavm.assets.TeavmVirtualFileSystem;

/**
 *
 * @author devnewton
 */
public class TeavmNuitAudio implements NuitAudio {

    private float effectsVolume = 1.0f;
    private float musicVolume = 1.0f;
    private final TeavmVirtualFileSystem vfs;

    public TeavmNuitAudio(TeavmVirtualFileSystem virtualFileSystem) {
        this.vfs = virtualFileSystem;
    }

    @Override
    public float getEffectsVolume() {
        return effectsVolume;
    }

    @Override
    public float getMusicVolume() {
        return musicVolume;
    }

    @Override
    public void setEffectsVolume(float v) {
        this.effectsVolume = v;
    }

    @Override
    public void setMusicVolume(float v) {
        this.musicVolume = v;
    }

    public void close() {

    }

    public void clearAll() {

    }

    @Override
    public void clearUseless() {
        clearAll();
    }

    class TeavmSound implements Sound {

        @Override
        public void play() {
            // TODO Auto-generated method stub

        }

        @Override
        public void stop() {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public Sound getSound(final String name) {
        return new TeavmSound();

    }

    @Override
    public void playMusic(final String name, final boolean loop) {
        if (musicVolume > 0.0f) {

        }
    }

    @Override
    public void stopMusic() {

    }
}
