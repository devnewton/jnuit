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
package im.bci.jnuit.lwjgl.assets;

import im.bci.jnuit.animation.ITexture;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.lwjgl.animation.LwjglAnimationCollection;
import java.lang.ref.ReferenceQueue;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devnewton
 */
public class GarbageCollectedAssets implements IAssets {

    private final AssetsLoader assets;
    private final HashMap<String/* name */, TextureWeakReference> textures = new HashMap<String/* name */, TextureWeakReference>();
    private final ReferenceQueue<LwjglTexture> texturesReferenceQueue = new ReferenceQueue<LwjglTexture>();
    private final HashMap<String/* name */, AnimationCollectionWeakReference> animations = new HashMap<String/* name */, AnimationCollectionWeakReference>();
    private final ReferenceQueue<LwjglAnimationCollection> animationsReferenceQueue = new ReferenceQueue<LwjglAnimationCollection>();
    private final HashMap<String/* name */, TrueTypeFontWeakReference> fonts = new HashMap<String/* name */, TrueTypeFontWeakReference>();
    private final ReferenceQueue<LwjglNuitFont> fontsReferenceQueue = new ReferenceQueue<LwjglNuitFont>();
    private static final Logger logger = Logger.getLogger(GarbageCollectedAssets.class.getName());

    public GarbageCollectedAssets(AssetsLoader assets) {
        this.assets = assets;
    }

    @Override
    public void clearAll() {
        for (TextureWeakReference ref : textures.values()) {
            ref.delete();
        }
        textures.clear();

        animations.clear();

        for (TrueTypeFontWeakReference ref : fonts.values()) {
            ref.delete();
        }
        fonts.clear();
    }

    @Override
    public void clearUseless() {
        System.gc();
        TextureWeakReference tex;
        while ((tex = (TextureWeakReference) texturesReferenceQueue.poll()) != null) {
            tex.delete();
        }
    }

    @Override
    public IAnimationCollection getAnimations(String name) {
        if (name.endsWith("png") || name.endsWith("jpg")) {
            return new TextureAnimationCollectionWrapper(this, name, 0, 0, 1, 1);
        } else {
            AnimationCollectionWeakReference animRef = animations.get(name);
            if (null != animRef) {
                LwjglAnimationCollection anim = animRef.get();
                if (null != anim) {
                    return anim;
                } else {
                    animations.remove(name);
                }
            }
            LwjglAnimationCollection anim = assets.loadAnimations(name);
            putAnim(name, anim);
            return anim;
        }
    }

    @Override
    public LwjglNuitFont getFont(String name) {
        TrueTypeFontWeakReference fontRef = fonts.get(name);
        if (fontRef != null) {
            LwjglNuitFont font = fontRef.get();
            if (font != null) {
                return font;
            } else {
                fonts.remove(name);
            }
        }
        LwjglNuitFont font = assets.loadFont(name);
        putFont(name, font);
        return font;
    }

    @Override
    public ITexture getTexture(String name) {
        TextureWeakReference textureRef = textures.get(name);
        if (textureRef != null) {
            LwjglTexture texture = textureRef.get();
            if (texture != null) {
                return texture;
            } else {
                textures.remove(name);
            }
        }
        LwjglTexture texture = assets.loadTexture(name);
        putTexture(name, texture);
        return texture;

    }

    @Override
    public TmxAsset getTmx(String name) {
        TmxAssetLoader tmxLoader = new TmxAssetLoader(this);
        return tmxLoader.loadTmx(name);
    }

    @Override
    public LwjglTexture grabScreenToTexture() {
        final String name = "!screenCapture_" + new Date().getTime();
        LwjglTexture texture = assets.grabScreenToTexture();
        putTexture(name, texture);
        return texture;
    }

    @Override
    public void setIcon(long glfwWindow, String name) {
        assets.setIcon(glfwWindow, name);
    }

    private void putAnim(String name, LwjglAnimationCollection anim) {
        animations.put(name, new AnimationCollectionWeakReference(name, anim, animationsReferenceQueue));
    }

    private void putTexture(String name, LwjglTexture texture) {
        textures.put(name, new TextureWeakReference(name, texture, texturesReferenceQueue));
    }

    private void putFont(String name, LwjglNuitFont font) {
        fonts.put(name, new TrueTypeFontWeakReference(name, font, fontsReferenceQueue));
    }

    @Override
    public String getText(String name) {
        return assets.loadText(name);
    }

    @Override
    public void forceAnimationUnload(String name) {
        clearUseless();
        AnimationCollectionWeakReference animation = animations.get(name);
        if (null != animation) {
            if (null != animation.get()) {
                logger.log(Level.WARNING, "Force still referenced animation ''{0}'' unload.", name);
            }
            animations.remove(name);
        }
    }

}
