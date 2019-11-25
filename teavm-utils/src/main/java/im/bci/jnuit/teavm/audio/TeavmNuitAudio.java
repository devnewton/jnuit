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
import im.bci.jnuit.teavm.assets.TeavmAssets;
import java.util.ArrayList;
import java.util.List;
import org.teavm.jso.dom.html.HTMLAudioElement;

/**
 *
 * @author devnewton
 */
public class TeavmNuitAudio implements NuitAudio {

    private float effectsVolume = 1.0f;
    private float musicVolume = 1.0f;
    private final TeavmAssets assets;
    private HTMLAudioElement music;
    private final List<TeavmSound> sounds = new ArrayList<>();

    public TeavmNuitAudio(TeavmAssets assets) {
        this.assets = assets;
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
        for(TeavmSound s : sounds) {
            s.audio.setVolume(effectsVolume);
        }
    }

    @Override
    public void setMusicVolume(float v) {
        if (null != this.music) {
            this.music.setVolume(musicVolume);
        }
        this.musicVolume = v;
    }

    public void clearAll() {
        this.music = null;
        this.sounds.clear();
        this.assets.clearAudios();
    }

    @Override
    public void clearUseless() {
        clearAll();
    }

    class TeavmSound implements Sound {

        private final HTMLAudioElement audio;

        private TeavmSound(HTMLAudioElement audio) {
            this.audio = audio;
        }

        @Override
        public void play() {
            audio.play();

        }

        @Override
        public void stop() {
            audio.pause();
        }

    }

    @Override
    public Sound getSound(final String name) {
        HTMLAudioElement audio = this.assets.getAudio(name);
        audio.setVolume(effectsVolume);
        TeavmSound sound = new TeavmSound(audio);
        sounds.add(sound);
        return sound;

    }

    @Override
    public void playMusic(final String name, final boolean loop) {
        if (musicVolume > 0.0f) {
            this.music = this.assets.getAudio(name);
            if (null != this.music) {
                this.music.setVolume(musicVolume);
                this.music.setLoop(loop);
                this.music.play();
            }
        }
    }

    @Override
    public void stopMusic() {
        if (null != this.music) {
            this.music.pause();
            this.music = null;
        }
    }
}
