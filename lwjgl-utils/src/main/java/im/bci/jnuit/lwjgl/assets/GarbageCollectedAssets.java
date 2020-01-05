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

import im.bci.jnuit.lwjgl.LwjglTexture;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.bci.jnuit.animation.ITexture;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.lwjgl.animation.LwjglAnimation;
import im.bci.jnuit.lwjgl.animation.LwjglAnimationCollection;
import im.bci.jnuit.lwjgl.animation.LwjglAnimationImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.ReferenceQueue;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class GarbageCollectedAssets implements IAssets {

    private final AssetsLoader assetsLoader;
    private final HashMap<String/* name */, TextureWeakReference> textures = new HashMap<String/* name */, TextureWeakReference>();
    private final ReferenceQueue<LwjglTexture> texturesReferenceQueue = new ReferenceQueue<LwjglTexture>();
    private final HashMap<String/* name */, AnimationCollectionWeakReference> animations = new HashMap<String/* name */, AnimationCollectionWeakReference>();
    private final ReferenceQueue<IAnimationCollection> animationsReferenceQueue = new ReferenceQueue<>();
    private final HashMap<String/* name */, TrueTypeFontWeakReference> fonts = new HashMap<String/* name */, TrueTypeFontWeakReference>();
    private final ReferenceQueue<LwjglNuitFont> fontsReferenceQueue = new ReferenceQueue<LwjglNuitFont>();
    private LwjglTextureQuality quality = LwjglTextureQuality.DEFAULT;

    private static final Logger LOGGER = Logger.getLogger(GarbageCollectedAssets.class.getName());

    public GarbageCollectedAssets(VirtualFileSystem vfs) {
        this.assetsLoader = new AssetsLoader(vfs);
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
            return getAnimationFromSubTexture(name, 0f, 0f, 1f, 1f);
        } else {
            return getAnimationsFromJson(name);
        }
    }

    private IAnimationCollection getAnimationsFromJson(String name) {
        AnimationCollectionWeakReference animRef = animations.get(name);
        if (null != animRef) {
            IAnimationCollection anim = animRef.get();
            if (null != anim) {
                return anim;
            } else {
                animations.remove(name);
            }
        }
        IAnimationCollection anim = loadAnimations(name);
        putAnim(name, anim);
        return anim;
    }

    @Override
    public IAnimationCollection getAnimationFromSubTexture(String name, float u1, float v1, float u2, float v2) {
        final String animationCollectionName = name + "#sub(" + u1 + "," + v1 + "," + u2 + "," + v2 + ")";

        AnimationCollectionWeakReference animRef = animations.get(animationCollectionName);
        if (null != animRef) {
            IAnimationCollection anim = animRef.get();
            if (null != anim) {
                return anim;
            } else {
                animations.remove(animationCollectionName);
            }
        }
        LwjglAnimationCollection animationCollection = new LwjglAnimationCollection();
        LwjglAnimation animation = new LwjglAnimation("default");
        LwjglAnimationImage image = new LwjglAnimationImage((LwjglTexture) getTexture(name));
        animation.addFrame(image, 24 * 60 * 60 * 1000, u1, v1, u2, v2);
        animationCollection.addAnimation(animation);
        putAnim(animationCollectionName, animationCollection);
        return animationCollection;
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
        LwjglNuitFont font = assetsLoader.loadFont(name);
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
        LwjglTexture texture = assetsLoader.loadTexture(name);
        putTexture(name, texture);
        return texture;

    }

    private void setupGLTextureQualityParams() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, quality.toGLTextureFilter());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, quality.toGLTextureFilter());
    }

    @Override
    public TmxAsset getTmx(String name) {
        TmxAssetLoader tmxLoader = new TmxAssetLoader(this);
        return tmxLoader.loadTmx(name);
    }

    @Override
    public LwjglTexture grabScreenToTexture() {
        final String name = "!screenCapture_" + new Date().getTime();
        LwjglTexture texture = assetsLoader.grabScreenToTexture();
        putTexture(name, texture);
        return texture;
    }

    @Override
    public void setIcon(long glfwWindow, String name) {
        assetsLoader.setIcon(glfwWindow, name);
    }

    private void putAnim(String name, IAnimationCollection anim) {
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
        return assetsLoader.loadText(name);
    }

    @Override
    public void forceAnimationUnload(String name) {
        clearUseless();
        AnimationCollectionWeakReference animation = animations.get(name);
        if (null != animation) {
            if (null != animation.get()) {
                LOGGER.log(Level.WARNING, "Force still referenced animation ''{0}'' unload.", name);
            }
            animations.remove(name);
        }
    }

    public void setQuality(LwjglTextureQuality newQuality) {
        if (newQuality != quality) {
            quality = newQuality;
            updateQuality();
        }
    }

    private void updateQuality() {
        for (TextureWeakReference ref : textures.values()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ref.textureId);
            setupGLTextureQualityParams();
        }
    }

    private LwjglAnimationCollection loadAnimations(String name) {
        if (name.endsWith(".json")) {
            return loadJsonNanim(name);
        } else {
            throw new RuntimeException("Unknow animation format for " + name);
        }
    }

    private LwjglAnimationCollection loadJsonNanim(String name) {
        LwjglAnimationCollection nanim = new LwjglAnimationCollection();
        try ( InputStream is = assetsLoader.getVfs().open(name);  InputStreamReader reader = new InputStreamReader(is)) {
            String nanimParentDir;
            final int lastIndexOfSlash = name.lastIndexOf("/");
            if (lastIndexOfSlash < 0) {
                nanimParentDir = "";
            } else {
                nanimParentDir = name.substring(0, name.lastIndexOf("/") + 1);
            }
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            for (JsonElement jsonAnimationElement : json.getAsJsonArray("animations")) {
                JsonObject jsonAnimation = jsonAnimationElement.getAsJsonObject();
                LwjglAnimation animation = new LwjglAnimation(jsonAnimation.get("name").getAsString());
                for (JsonElement jsonFrameElement : jsonAnimation.getAsJsonArray("frames")) {
                    JsonObject jsonFrame = jsonFrameElement.getAsJsonObject();
                    final String imageFilename = nanimParentDir + jsonFrame.get("image").getAsString();
                    LwjglTexture texture = (LwjglTexture) getTexture(imageFilename);
                    LwjglAnimationImage image = new LwjglAnimationImage(texture);
                    animation.addFrame(image, jsonFrame.get("duration").getAsInt(), jsonFrame.get("u1").getAsFloat(),
                            jsonFrame.get("v1").getAsFloat(), jsonFrame.get("u2").getAsFloat(),
                            jsonFrame.get("v2").getAsFloat());
                }
                nanim.addAnimation(animation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load animation " + name, e);
        }
        return nanim;
    }

}
