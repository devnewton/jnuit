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

import im.bci.jnuit.lwjgl.LwjglNuitFont;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class TrueTypeFontWeakReference extends WeakReference<LwjglNuitFont> {

    Integer textureId;
    String name;
    private static final Logger logger = Logger.getLogger(TrueTypeFontWeakReference.class.getName());

    TrueTypeFontWeakReference(String name, LwjglNuitFont font, ReferenceQueue<LwjglNuitFont> queue) {
        super(font, queue);
        textureId = font.getFontTextureID();
        this.name = name;
    }

    void delete() {
        if (null != textureId) {
            logger.log(Level.FINE, "Unload font {0}", name);
            ByteBuffer temp = ByteBuffer.allocateDirect(4);
            temp.order(ByteOrder.nativeOrder());
            IntBuffer intBuffer = temp.asIntBuffer();
            intBuffer.put(textureId);
            GL11.glDeleteTextures(intBuffer);
            textureId = null;
        }
    }
}
