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

import im.bci.jnuit.animation.IAnimationCollection;
import java.util.logging.Level;
import java.util.logging.Logger;
import playn.core.Assets;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;

/**
 *
 * @author devnewton
 */
public class PlaynAnimationLoader {

    private static final Logger LOGGER = Logger.getLogger(PlaynAnimationLoader.class.getName());

    public static IAnimationCollection load(Assets assets, String animationFilename) {
        if (animationFilename.endsWith(".png") || animationFilename.endsWith(".jpg")) {
            return loadImage(assets, animationFilename);
        } else {
            return loadNanim(assets, animationFilename);
        }
    }

    public static IAnimationCollection loadImage(Assets assets, String filename) {
        PlaynAnimationCollection animations = new PlaynAnimationCollection();
        final PlaynAnimation animation = new PlaynAnimation(filename);
        animation.addFrame(new PlaynAnimationImage(assets.getImage(filename)), 1000000);
        animations.addAnimation(animation);
        animations.setReady(true);
        return animations;
    }

    public static IAnimationCollection loadNanim(final Assets assets, final String filename) {
        final PlaynAnimationCollection nanim = new PlaynAnimationCollection();
        assets.getText(filename, new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String path;
                    final int lastIndexOfSlash = filename.lastIndexOf("/");
                    if (lastIndexOfSlash < 0) {
                        path = "";
                    } else {
                        path = filename.substring(0, filename.lastIndexOf("/") + 1);
                    }

                    Json.Object json = PlayN.json().parse(result);

                    Json.Array jsonAnimations = json.getArray("animations");
                    for (int a = 0, na = jsonAnimations.length(); a < na; ++a) {
                        Json.Object jsonAnimation = jsonAnimations.getObject(a);
                        PlaynAnimation animation = new PlaynAnimation(jsonAnimation.getString("name"));
                        Json.Array jsonFrames = jsonAnimation.getArray("frames");
                        for (int f = 0, nf = jsonFrames.length(); f < nf; ++f) {
                            Json.Object jsonFrame = jsonFrames.getObject(f);
                            final String imageFilename = jsonFrame.getString("image");
                            PlaynAnimationImage image = new PlaynAnimationImage(assets.getImage(path + imageFilename));
                            animation.addFrame(image, jsonFrame.getInt("duration"), jsonFrame.getNumber("u1"), jsonFrame.getNumber("v1"), jsonFrame.getNumber("u2"), jsonFrame.getNumber("v2"));
                        }
                        nanim.addAnimation(animation);
                    }
                    nanim.setReady(true);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error loading " + filename, ex);
                }
            }

            @Override
            public void onFailure(Throwable cause) {
                LOGGER.log(Level.SEVERE, "Cannot load " + filename, cause);
            }
        });
        return new NotReadyPlaynAnimationCollection(nanim);
    }
}
