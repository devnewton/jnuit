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
package im.bci.jnuit.lwjgl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;

public class IconLoader {

    public static void setIcon(long glfwWindow, InputStream is) {
        try {
            GLFW.glfwSetWindowIcon(glfwWindow, loadIcon(is));
        } catch (Exception ex) {
            Logger.getLogger(IconLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static GLFWImage.Buffer loadIcon(InputStream is) throws IOException {
        GLFWImage.Buffer images = GLFWImage.create(1);
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer bppBuffer = BufferUtils.createIntBuffer(1);
        byte[] bytes = is.readAllBytes();
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
        buffer.put(bytes).flip();
        ByteBuffer pixels = STBImage.stbi_load_from_memory(buffer, widthBuffer, heightBuffer, bppBuffer, 0);
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        GLFWImage image = GLFWImage.create();
        image.set(width, height, pixels);
        images.put(image);
        images.flip();
        return images;
    }

}