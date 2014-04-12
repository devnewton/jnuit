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
package im.bci.jnuit.playn;

import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.audio.Sound;
import playn.core.CachingAssets;
import playn.core.PlayN;

/**
 *
 * @author devnewton
 */
public class PlaynNuitAudio implements NuitAudio {

    private final CachingAssets assets = new CachingAssets(PlayN.assets());
    private playn.core.Sound music;
    private float musicVolume = 1.0f;
    private float effectsVolume = 1.0f;

    @Override
    public float getEffectsVolume() {
        return effectsVolume;
    }

    @Override
    public float getMusicVolume() {
        return 1.0f;
    }

    @Override
    public void setEffectsVolume(float v) {
        this.effectsVolume = v;
    }

    @Override
    public void setMusicVolume(float v) {
        this.musicVolume = v;
        if (null != music) {
            music.setVolume(musicVolume);
        }
    }

    @Override
    public Sound getSound(final String name) {
        String baseName = name.substring(0, name.lastIndexOf('.'));
        final playn.core.Sound sound = assets.getSound(baseName);
        sound.setVolume(effectsVolume);
        return new Sound() {

            @Override
            public void play() {
                sound.play();
            }

            @Override
            public void stop() {
                sound.stop();
            }
        };
    }

    @Override
    public void playMusic(String name) {
        stopMusic();
        String baseName = name.substring(0, name.lastIndexOf('.'));
        music = assets.getMusic(baseName);
        music.setLooping(true);
        music.setVolume(musicVolume);
        music.play();
    }

    @Override
    public void stopMusic() {
        if (null != music) {
            music.stop();
        }
    }

    @Override
    public void clearUseless() {
    }

}
