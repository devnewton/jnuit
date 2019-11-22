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
package im.bci.jnuit.teavm.assets.animation;

import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.animation.PlayMode;

/**
 *
 * @author devnewton
 */
class NotReadyPlay implements IPlay {

    private final TeavmAnimationCollection animationCollection;
    private final String animationName;
    private PlayMode mode;
    private TeavmAnimationPlayState state = TeavmAnimationPlayState.STOPPED;
    private IPlay realPlay;

    public NotReadyPlay(TeavmAnimationCollection animationCollection, String animationName, PlayMode mode) {
        this.animationCollection = animationCollection;
        this.animationName = animationName;
        this.mode = mode;
        state = TeavmAnimationPlayState.STARTED;
    }

    @Override
    public String getName() {
        if(null != animationName) {
            return animationName;
        } else if(null != realPlay){
            return realPlay.getName();            
        } else {
            return "first";
        }
        
    }

    @Override
    public void start(PlayMode mode) {
        this.mode = mode;
        state = TeavmAnimationPlayState.STARTED;
        if (null != realPlay) {
            realPlay.start(mode);
        }
    }

    @Override
    public void stop() {
        state = TeavmAnimationPlayState.STOPPED;
        if (null != realPlay) {
            realPlay.stop();
        }
    }

    @Override
    public boolean isStopped() {
        return state == TeavmAnimationPlayState.STOPPED;
    }

    @Override
    public PlayMode getMode() {
        return mode;
    }

    @Override
    public void update(long elapsedTime) {
        if (null != realPlay) {
            realPlay.update(elapsedTime);
        } else if (state == TeavmAnimationPlayState.STARTED && animationCollection.isReady()) {
            if(null != animationName) {
                realPlay = animationCollection.getAnimationByName(animationName).start(mode);
            } else {
                realPlay = animationCollection.getFirst().start(mode);
            }
        }
    }

    @Override
    public IAnimationFrame getCurrentFrame() {
        if (null != realPlay) {
            return realPlay.getCurrentFrame();
        } else {
            return null;
        }
    }

    @Override
    public void restart() {
        stop();
        start(mode);
    }

}
