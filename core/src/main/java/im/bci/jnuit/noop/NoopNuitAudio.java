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
package im.bci.jnuit.noop;

import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.audio.Sound;

/**
 *
 * @author devnewton
 */
public class NoopNuitAudio implements NuitAudio {

    private float effectsVolume = 1.0f;
    private float musicVolume = 1.0f;

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
        effectsVolume = v;
    }

    @Override
    public void setMusicVolume(float v) {
        musicVolume = v;
    }

    @Override
    public Sound getSound(String name) {
        return new Sound() {

            @Override
            public void play() {
            }

            @Override
            public void stop() {
            }
        };
    }

    @Override
    public void playMusic(String name) {
    }

    @Override
    public void stopMusic() {
    }

    @Override
    public void clearUseless() {
    }

}
