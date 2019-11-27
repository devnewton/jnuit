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
package im.bci.jnuit.teavm.assets.animation;

import im.bci.jnuit.teavm.JsonMap;
import im.bci.jnuit.teavm.JsonArray;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.teavm.assets.TeavmAssets;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;
import org.teavm.jso.json.JSON;

/**
 *
 * @author devnewton
 */
public class TeavmAnimationLoader {

    public static IAnimationCollection load(TeavmAssets assets, String animationFilename) {
        if (animationFilename.endsWith(".png") || animationFilename.endsWith(".jpg")) {
            return loadImage(assets, animationFilename);
        } else {
            return loadNanim(assets, animationFilename);
        }
    }

    public static IAnimationCollection loadImage(TeavmAssets assets, String filename) {
        TeavmAnimationCollection animations = new TeavmAnimationCollection();
        final TeavmAnimation animation = new TeavmAnimation(filename);
        animation.addFrame(new TeavmAnimationImage(assets.getImage(filename)), 1000000);
        animations.addAnimation(animation);
        return animations;
    }



    public static IAnimationCollection loadNanim(final TeavmAssets assets, final String filename) {
        try {
                String path;
                final int lastIndexOfSlash = filename.lastIndexOf("/");
                if (lastIndexOfSlash < 0) {
                    path = "";
                } else {
                    path = filename.substring(0, filename.lastIndexOf("/") + 1);
                }
                final String text = assets.getText(filename);
                final JsonMap json = JSON.parse(text).cast();
                final JsonArray jsonAnimations = json.get("animations").cast();
                final TeavmAnimationCollection nanim = new TeavmAnimationCollection();
                for (int a = 0, na = jsonAnimations.getLength(); a < na; ++a) {
                    final JsonMap jsonAnimation = jsonAnimations.get(a).cast();
                    final JSString animationName = jsonAnimation.get("name").cast();
                    TeavmAnimation animation = new TeavmAnimation(animationName.stringValue());
                    final JsonArray jsonFrames = jsonAnimation.get("frames").cast();
                    for (int f = 0, nf = jsonFrames.getLength(); f < nf; ++f) {
                        final JsonMap jsonFrame = jsonFrames.get(f).cast();
                        final JSString jsImageFilename = jsonFrame.get("image").cast();
                        final String imageFilename = jsImageFilename.stringValue();
                        TeavmAnimationImage image = new TeavmAnimationImage(assets.getImage(path + imageFilename));
                        JSNumber duration = jsonFrame.get("duration").cast();
                        JSNumber u1 = jsonFrame.get("u1").cast();
                        JSNumber v1 = jsonFrame.get("v1").cast();
                        JSNumber u2 = jsonFrame.get("u2").cast();
                        JSNumber v2 = jsonFrame.get("v2").cast();                        
                        animation.addFrame(image, duration.intValue(), u1.floatValue(), v1.floatValue(), u2.floatValue(), v2.floatValue());
                    }
                    nanim.addAnimation(animation);
                }
                return nanim;
        } catch (Exception ex) {
            throw new RuntimeException("Cannot load animation " + filename, ex);
        }
    }
}
