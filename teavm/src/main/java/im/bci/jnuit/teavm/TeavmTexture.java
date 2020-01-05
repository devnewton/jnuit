package im.bci.jnuit.teavm;

import im.bci.jnuit.animation.ITexture;
import org.teavm.jso.dom.html.HTMLImageElement;

/**
 *
 * @author devnewton
 */
public class TeavmTexture implements ITexture {

    HTMLImageElement image;

    public TeavmTexture(HTMLImageElement image) {
        this.image = image;
    }

    public HTMLImageElement getImage() {
        return image;
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }
}
