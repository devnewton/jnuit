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
package im.bci.jnuit.lwjgl.animation;

import im.bci.jnuit.animation.IAnimationImage;
import im.bci.jnuit.lwjgl.assets.Texture;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class NanimationImage implements IAnimationImage {

    private final int id;
    private final boolean alpha;

    public NanimationImage(boolean alpha) {
        ByteBuffer temp = ByteBuffer.allocateDirect(4);
        temp.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = temp.asIntBuffer();
        GL11.glGenTextures(intBuffer);
        id = intBuffer.get(0);
        this.alpha = alpha;
    }

    public NanimationImage(Texture texture) {
        this.id = texture.getId();
        this.alpha = texture.hasAlpha();
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean hasAlpha() {
        return alpha;
    }
}
