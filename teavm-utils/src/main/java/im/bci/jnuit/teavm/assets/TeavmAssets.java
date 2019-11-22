package im.bci.jnuit.teavm.assets;

public class TeavmAssets {

    private final TeavmVirtualFileSystem vfs;

    public TeavmAssets(TeavmVirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public String getImage(String filename) {
        return filename;
    }

}
