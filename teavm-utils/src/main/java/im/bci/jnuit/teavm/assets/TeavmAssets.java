package im.bci.jnuit.teavm.assets;

import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.teavm.assets.animation.TeavmAnimationLoader;
import java.util.HashMap;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLImageElement;

public class TeavmAssets {

    private final TeavmVirtualFileSystem vfs;
    private final HashMap<String, IAnimationCollection> animations = new HashMap<>();
    private final HashMap<String, HTMLImageElement> images = new HashMap<>();

    public TeavmAssets(TeavmVirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public HTMLImageElement getImage(String filename) {
        HTMLImageElement image = images.get(filename);
        if (null == image) {
            image = Window.current().getDocument().createElement("img").cast();
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

}
