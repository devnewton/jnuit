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
package im.bci.jnuit.background;

import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.visitors.BackgroundVisitor;
import im.bci.jnuit.widgets.Widget;

/**
 * Textured widget background.
 * @author devnewton
 */
public class TexturedBackground implements Background {

    private final IPlay play;
    private boolean mirrorX, mirrorY;

    public TexturedBackground(IPlay play) {
        this.play = play;
    }

    public IPlay getPlay() {
        return play;
    }

    public boolean isMirrorX() {
        return mirrorX;
    }

    public void setMirrorX(boolean mirrorX) {
        this.mirrorX = mirrorX;
    }

    public boolean isMirrorY() {
        return mirrorY;
    }

    public void setMirrorY(boolean mirrorY) {
        this.mirrorY = mirrorY;
    }

    @Override
    public void accept(Widget widget, BackgroundVisitor visitor) {
        visitor.visit(widget, this);
    }

    @Override
    public void update(float delta) {
        play.update((long) (delta * 1000L));
    }

}
