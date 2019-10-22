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

import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.playn.animation.PlaynAnimationLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import playn.core.PlayN;

/**
 *
 * @author devnewton
 */
public class TimeBasedCachedAssets {

    private final HashMap<String, CachedAnimation> animations = new HashMap<String, CachedAnimation>();
    private final double timeToLive;

    public TimeBasedCachedAssets(double timeToLive) {
        this.timeToLive = timeToLive;
    }
    
    private static abstract class AbstractCached {

        double timeToDie;
    }

    private static class CachedAnimation extends AbstractCached {

        IAnimationCollection animations;
    }

    public IAnimationCollection getAnimations(String name) {
        CachedAnimation cachedAnimation = animations.get(name);
        if (null == cachedAnimation) {
            cachedAnimation = new CachedAnimation();
            cachedAnimation.animations = PlaynAnimationLoader.load(PlayN.assets(), name);
            animations.put(name, cachedAnimation);
        }
        cachedAnimation.timeToDie = PlayN.currentTime() + timeToLive;
        return cachedAnimation.animations;
    }

    public void clearUseless() {
        Iterator<Map.Entry<String, CachedAnimation>> it = animations.entrySet().iterator();
        double currentTime = PlayN.currentTime();
        while (it.hasNext()) {
            Map.Entry<String, CachedAnimation> entry = it.next();
            if (currentTime > entry.getValue().timeToDie) {
                it.remove();
            }
        }
    }

}
