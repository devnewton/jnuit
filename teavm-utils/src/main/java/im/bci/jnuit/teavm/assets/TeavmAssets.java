package im.bci.jnuit.teavm.assets;

import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.teavm.Ajax;
import im.bci.jnuit.teavm.assets.animation.TeavmAnimationLoader;
import java.io.IOException;
import java.util.HashMap;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLAudioElement;
import org.teavm.jso.dom.html.HTMLImageElement;

public class TeavmAssets {

    private final TeavmVirtualFileSystem vfs;
    private final HashMap<String, IAnimationCollection> animations = new HashMap<>();
    private final HashMap<String, HTMLImageElement> images = new HashMap<>();
    private final HashMap<String, HTMLAudioElement> audios = new HashMap<>();
    private final Window current = Window.current();

    public TeavmAssets(TeavmVirtualFileSystem vfs) {
        this.vfs = vfs;
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

    public HTMLImageElement getImage(String filename) {
        HTMLImageElement image = images.get(filename);
        if (null == image) {
            image = current.getDocument().createElement("img").cast();
            image.setSrc(vfs.getRealResourcePath(filename));
            images.put(filename, image);
        }
        return image;
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

}
