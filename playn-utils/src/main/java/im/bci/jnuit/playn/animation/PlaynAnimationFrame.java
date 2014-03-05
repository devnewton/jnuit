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
package im.bci.jnuit.playn.animation;

import im.bci.jnuit.animation.IAnimationFrame;

/**
 *
 * @author devnewton
 */
public class PlaynAnimationFrame implements IAnimationFrame {

    private final PlaynAnimationImage image;
    private final long duration;//milliseconds
    long endTime;//milliseconds
    final float u1;
    final float v1;
    final float u2;
    final float v2;

    public PlaynAnimationFrame(PlaynAnimationImage image, long duration) {
        this.image = image;
        this.duration = duration;
        u1 = 0;
        v1 = 0;
        u2 = 1;
        v2 = 1;
    }

    PlaynAnimationFrame(PlaynAnimationImage image, long duration, float u1, float v1, float u2, float v2) {
        this.image = image;
        this.duration = duration;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public PlaynAnimationImage getImage() {
        return image;
    }

    @Override
    public float getU1() {
        return u1;
    }

    @Override
    public float getV1() {
        return v1;
    }

    @Override
    public float getU2() {
        return u2;
    }

    @Override
    public float getV2() {
        return v2;
    }
}
