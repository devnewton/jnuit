/*
 The MIT License (MIT)

 Copyright (c) 2013 devnewton <devnewton@bci.im>

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
package im.bci.jnuit.widgets;

import java.util.ArrayList;
import java.util.List;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.visitors.WidgetVisitor;

/**
 * Audio settings widget.
 *
 * @author devnewton
 */
public class AudioConfigurator extends Table {

    protected NuitToolkit toolkit;
    protected Select<Volume> musicSelect;
    protected Select<Volume> effectsSelect;

    public static class Volume {

        int level;

        Volume(int level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return level + "%";
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + this.level;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Volume other = (Volume) obj;
            return this.level == other.level;
        }

    }

    public AudioConfigurator(NuitToolkit toolkit) {
        super(toolkit);
        this.toolkit = toolkit;
        List<Volume> possibleVolumes = new ArrayList<>();
        for (int l = 0; l <= 100; l += 10) {
            possibleVolumes.add(new Volume(l));
        }

        defaults().expand();
        cell(new Label(toolkit, "nuit.audio.configurator.music.volume"));
        musicSelect = new Select<Volume>(toolkit, possibleVolumes) {
            @Override
            public void onLeft() {
                super.onLeft();
                changeMusicVolume(getSelected().level / 100.0f);
            }

            @Override
            public void onRight() {
                super.onRight();
                changeMusicVolume(getSelected().level / 100.0f);
            }
        };
        musicSelect.setSelected(new Volume(Math.round(toolkit.getAudio().getMusicVolume() * 10f) * 10));
        cell(musicSelect);
        row();
        cell(new Label(toolkit, "nuit.audio.configurator.effects.volume"));
        effectsSelect = new Select<Volume>(toolkit, possibleVolumes) {
            @Override
            public void onLeft() {
                super.onLeft();
                changeEffectsVolume(getSelected().level / 100.0f);
            }

            @Override
            public void onRight() {
                super.onRight();
                changeEffectsVolume(getSelected().level / 100.0f);
            }
        };
        effectsSelect.setSelected(new Volume(Math.round(toolkit.getAudio().getEffectsVolume() * 10f) * 10));
        cell(effectsSelect);
        row();
        cell(new Button(toolkit, "nuit.audio.configurator.back") {
            @Override
            public void onOK() {
                AudioConfigurator.this.close();
            }
        }).colspan(2);
    }

    protected void changeEffectsVolume(float f) {
        toolkit.getAudio().setEffectsVolume(f);
    }

    protected void changeMusicVolume(float f) {
        toolkit.getAudio().setMusicVolume(f);
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
