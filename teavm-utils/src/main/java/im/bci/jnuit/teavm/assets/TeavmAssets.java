package im.bci.jnuit.teavm.assets;

import im.bci.jnuit.teavm.TeavmTexture;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.teavm.Ajax;
import im.bci.jnuit.teavm.assets.animation.TeavmAnimation;
import im.bci.jnuit.teavm.assets.animation.TeavmAnimationCollection;
import im.bci.jnuit.teavm.assets.animation.TeavmAnimationImage;
import java.io.IOException;
import java.util.HashMap;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLAudioElement;
import org.teavm.jso.dom.html.HTMLImageElement;

public class TeavmAssets {

    private final TeavmVirtualFileSystem vfs;
    private final HashMap<String, IAnimationCollection> animations = new HashMap<>();
    private final HashMap<String, TeavmTexture> images = new HashMap<>();
    private final HashMap<String, HTMLAudioElement> audios = new HashMap<>();
    private final Window current = Window.current();

    public TeavmAssets(TeavmVirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public TeavmVirtualFileSystem getVfs() {
        return vfs;
    }

    public String getText(String filename) {
        try {
            return Ajax.get(vfs.getRealResourcePath(filename));
        } catch (IOException ex) {
            System.err.println("Cannot get text file " + filename + " :" + ex);
            return null;
        }
    }

    public HTMLAudioElement getAudio(String filename) {
        HTMLAudioElement audio = audios.get(filename);
        if (null == audio) {
            audio = current.getDocument().createElement("audio").cast();
            audio.setSrc(vfs.getRealResourcePath(filename));
            audios.put(filename, audio);
        }
        return audio;
    }

    public TeavmTexture getTexture(String filename) {
        TeavmTexture texture = images.get(filename);
        if (null == texture) {
            HTMLImageElement image = current.getDocument().createElement("img").cast();
            image.setSrc(vfs.getRealResourcePath(filename));
            images.put(filename, texture);
        }
        return texture;
    }

    public IAnimationCollection getAnimations(String filename) {
        IAnimationCollection animation = animations.get(filename);
        if (null == animation) {
            animation = TeavmAnimationLoader.load(this, filename);
            animations.put(filename, animation);
        }
        return animation;
    }

    public void clearAll() {
        animations.clear();
    }

    public void clearAudios() {
        for (HTMLAudioElement a : audios.values()) {
            a.pause();
        }
        audios.clear();
    }

    public IAnimationCollection getAnimationFromSubTexture(String name, float u1, float v1, float u2, float v2) {
        final String animationCollectionName = name + "#sub(" + u1 + "," + v1 + "," + u2 + "," + v2 + ")";
        TeavmAnimationCollection animationCollection = (TeavmAnimationCollection) animations.get(animationCollectionName);
        if (null != animationCollection) {
            animationCollection = new TeavmAnimationCollection();
            TeavmAnimation animation = new TeavmAnimation("default");
            animation.addFrame(new TeavmAnimationImage(getTexture(name)), 24 * 60 * 60 * 1000, u1, v1, u2, v2);
            animationCollection.addAnimation(animation);
            animations.put(animationCollectionName, animationCollection);            
        }
        return animationCollection;
    }

}
