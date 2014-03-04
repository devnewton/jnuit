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
package im.bci.jnuit.playn.animation;

import im.bci.jnuit.animation.IAnimationCollection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaynAnimationCollection implements IAnimationCollection {

    final LinkedHashMap<String/*animation name*/, PlaynAnimation> animations = new LinkedHashMap<>();
    private final Map<String, PlaynAnimationImage> images = new HashMap<>();
    private boolean ready;

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void addAnimation(PlaynAnimation animation) {
        animations.put(animation.getName(), animation);
    }

    @Override
    public PlaynAnimation getFirst() {
        return animations.values().iterator().next();
    }

    @Override
    public PlaynAnimation getAnimationByName(String name) {
        PlaynAnimation nanimation = animations.get(name);
        if (null != nanimation) {
            return nanimation;
        } else {
            throw new RuntimeException("Unknown animation " + name);
        }
    }

    public Map<String, PlaynAnimationImage> getImages() {
        return images;
    }
}
