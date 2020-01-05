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

import im.bci.jnuit.lwjgl.LwjglTexture;
import im.bci.jnuit.lwjgl.IconLoader;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author devnewton
 */
public class AssetsLoader {

    private VirtualFileSystem vfs;
    private static final Logger LOGGER = Logger.getLogger(AssetsLoader.class.getName());

    public AssetsLoader(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public VirtualFileSystem getVfs() {
        return vfs;
    }

    public void setVfs(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public LwjglTexture loadTexture(String name) {
        LOGGER.log(Level.FINE, "Load texture {0}", name);
        try ( InputStream is = vfs.open(name)) {
            IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
            IntBuffer bppBuffer = BufferUtils.createIntBuffer(1);
            byte[] bytes = is.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes).flip();
            ByteBuffer pixels = STBImage.stbi_load_from_memory(buffer, widthBuffer, heightBuffer, bppBuffer, 0);
            int width = widthBuffer.get();
            int height = heightBuffer.get();
            int bpp = bppBuffer.get();
            int pixelFormat = bpp == 4 ? GL11.GL_RGBA : GL11.GL_RGB;
            LwjglTexture texture = new LwjglTexture(width, height, pixelFormat == GL11.GL_RGBA);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            LwjglHelper.setupGLTextureParams();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelFormat, width, height, 0, pixelFormat, GL11.GL_UNSIGNED_BYTE,
                    pixels);
            return texture;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load texture " + name, e);
        }
    }

    public LwjglTexture grabScreenToTexture() {
        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        LwjglTexture texture = new LwjglTexture(Math.min(maxSize, viewport[2]), Math.min(maxSize, viewport[3]), false);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        LwjglHelper.setupGLTextureParams();
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, texture.getWidth(), texture.getHeight(), 0);
        return texture;
    }

    public String loadText(String name) {
        try ( InputStream is = vfs.open(name);  Scanner s = new Scanner(is, "UTF-8")) {
            s.useDelimiter("\\Z");
            return s.next();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Cannot load text file: " + name, ex);
        }
    }

    public LwjglNuitFont loadFont(String params) {
        LOGGER.log(Level.FINE, "Load font {0}", params);
        String fontName = "";
        int fontSize = 24;
        int fontStyle = Font.PLAIN;
        boolean antialising = false;
        for (String param : params.split(",")) {
            if ("bold".equalsIgnoreCase(param)) {
                fontStyle |= Font.BOLD;
            } else if ("italic".equalsIgnoreCase(param)) {
                fontStyle |= Font.ITALIC;
            } else if ("antialiasing".equalsIgnoreCase(param)) {
                antialising = true;
            } else {
                try {
                    fontSize = Integer.parseInt(param);
                } catch (NumberFormatException ex) {
                    fontName = param;
                }
            }
        }
        Font f;
        try ( InputStream is = vfs.open(fontName)) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            f = f.deriveFont(fontStyle, fontSize);
        } catch (Exception e) {
            f = new Font("monospaced", fontStyle, fontSize);
        }
        LwjglNuitFont font = new FontAsset(f, antialising, new char[0], new HashMap<Character, BufferedImage>());
        font.setCorrection(false);
        return font;
    }

    public void setIcon(long glfwWindow, String name) {
        try ( InputStream is = vfs.open(name)) {
            IconLoader.setIcon(glfwWindow, is);
        } catch (Exception ex) {
            Logger.getLogger(AssetsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class FontAsset extends LwjglNuitFont {

        public FontAsset(Font font, boolean antiAlias, char[] additionalChars,
                Map<Character, BufferedImage> specialCharacters) {
            super(font, antiAlias, additionalChars, specialCharacters);
        }

        @Override
        public void deleteFontTexture() {
            // NOTHING
        }
    }
}
