package im.bci.jnuit.teavm;

import im.bci.jnuit.animation.ITexture;
import org.teavm.interop.Async;
import org.teavm.jso.JSBody;
import org.teavm.jso.canvas.CanvasImageSource;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLImageElement;

/**
 *
 * @author devnewton
 */
public class TeavmTexture implements ITexture {

    CanvasImageSource image;
    int width, height;

    public TeavmTexture(HTMLImageElement image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public TeavmTexture(HTMLCanvasElement canvas) {
        this.image = createImageBitmap(canvas);
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
    }

    @Async
    @JSBody(params = "canvas", script = "return createImageBitmap(canvas);")
    public static native CanvasImageSource createImageBitmap(HTMLCanvasElement canvas);

    public CanvasImageSource getImage() {
        return image;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
