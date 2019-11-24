package im.bci.jnuit.teavm.assets;

import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.teavm.assets.animation.TeavmAnimationLoader;

public class TeavmAssets {

    private final TeavmVirtualFileSystem vfs;

    public TeavmAssets(TeavmVirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public String getImage(String filename) {
        return vfs.getRealResourcePath(filename);
    }
    
    public IAnimationCollection getAnimations(String filename) {
        return TeavmAnimationLoader.load(this, filename);
    }

}
