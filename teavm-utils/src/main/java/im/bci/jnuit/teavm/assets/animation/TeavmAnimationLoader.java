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

import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.teavm.assets.TeavmAssets;
import java.util.logging.Level;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.core.JSArray;
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
        animations.setReady(true);
        return animations;
    }

    public interface JsonMap {

        @JSIndexer
        JSObject get(String propertyName);
    }

    public interface JsonArray {

        @JSIndexer
        JSObject get(int index);
        
        @JSProperty
        int getLength();
    }

    public static IAnimationCollection loadNanim(final TeavmAssets assets, final String filename) {
        final TeavmAnimationCollection nanim = new TeavmAnimationCollection();
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.onComplete(() -> {
            String path;
            final int lastIndexOfSlash = filename.lastIndexOf("/");
            if (lastIndexOfSlash < 0) {
                path = "";
            } else {
                path = filename.substring(0, filename.lastIndexOf("/") + 1);
            }
            JsonMap json = JSON.parse(xhr.getResponseText()).cast();
            JsonArray jsonAnimations = json.get("animations").cast();
            for (int a = 0, na = jsonAnimations.getLength(); a < na; ++a) {
                JsonMap jsonAnimation = jsonAnimations.get(a).cast();
                TeavmAnimation animation = new TeavmAnimation(jsonAnimation.get("name").cast());
                JsonArray jsonFrames = jsonAnimation.get("frames").cast();
                for (int f = 0, nf = jsonFrames.getLength(); f < nf; ++f) {
                    JsonMap jsonFrame = jsonFrames.get(f).cast();
                    final String imageFilename = jsonFrame.get("image").cast();
                    TeavmAnimationImage image = new TeavmAnimationImage(assets.getImage(path + imageFilename));
                    animation.addFrame(image, jsonFrame.get("duration").cast(), jsonFrame.get("u1").cast(), jsonFrame.get("v1").cast(), jsonFrame.get("u2").cast(), jsonFrame.get("v2").cast());
                }
                nanim.addAnimation(animation);
            }
            nanim.setReady(true);

        });
        xhr.open("GET", filename);
        xhr.send();
        return new NotReadyTeavmAnimationCollection(nanim);
    }
}
