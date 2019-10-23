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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.bci.jnuit.lwjgl.IconLoader;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.lwjgl.animation.NanimationCollection;
import im.bci.jnuit.lwjgl.animation.Nanimation;
import im.bci.jnuit.lwjgl.animation.NanimationImage;
import im.bci.jnuit.lwjgl.animation.NanimParser.Nanim;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.Scanner;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author devnewton
 */
public class AssetsLoader {

    private VirtualFileSystem vfs;
    private static final Logger logger = Logger.getLogger(AssetsLoader.class.getName());

    public AssetsLoader(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public void setVfs(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public Texture loadTexture(String name) {
        logger.log(Level.FINE, "Load texture {0}", name);
        try (InputStream is = vfs.open(name)) {
            IntBuffer width = IntBuffer.allocate(1);
            IntBuffer height = IntBuffer.allocate(1);
            IntBuffer bpp = IntBuffer.allocate(1);
            ByteBuffer buffer = ByteBuffer.wrap(is.readAllBytes());
            ByteBuffer pixels = STBImage.stbi_load_from_memory(buffer, width, height, bpp, 0);
            int pixelFormat = bpp.get() == 4 ? GL11.GL_RGBA : GL11.GL_RGB;
            Texture texture = new Texture(width.get(), height.get(), pixelFormat == GL11.GL_RGBA);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            LwjglHelper.setupGLTextureParams();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelFormat, width.get(), height.get(), 0, pixelFormat,
                    GL11.GL_UNSIGNED_BYTE, pixels);
            return texture;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load texture " + name, e);
        }
    }

    public NanimationCollection loadAnimations(String name) {
        if (name.endsWith(".nanim") || name.endsWith(".nanim.gz")) {
            return loadNanim(name);
        } else if (name.endsWith(".json")) {
            return loadJsonNanim(name);
        } else {
            throw new RuntimeException("Unknow animation format for " + name);
        }
    }

    private NanimationCollection loadNanim(String name) throws RuntimeException {
        try {
            InputStream vfsInputStream = vfs.open(name);
            try {
                logger.log(Level.FINE, "Load animation {0}", name);
                InputStream is;
                if (name.endsWith(".gz")) {
                    is = new GZIPInputStream(vfsInputStream);
                } else {
                    is = vfsInputStream;
                }
                NanimationCollection anim = new NanimationCollection(Nanim.parseFrom(is));
                return anim;
            } finally {
                vfsInputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load animation " + name, e);
        }
    }

    public Texture grabScreenToTexture() {
        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Texture texture = new Texture(Math.min(maxSize, viewport[2]), Math.min(maxSize, viewport[3]), false);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        LwjglHelper.setupGLTextureParams();
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, texture.getWidth(), texture.getHeight(), 0);
        return texture;
    }

    public String loadText(String name) {
        try (InputStream is = vfs.open(name); Scanner s = new Scanner(is, "UTF-8")) {
            s.useDelimiter("\\Z");
            return s.next();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Cannot load text file: " + name, ex);
        }
    }

    public LwjglNuitFont loadFont(String params) {
        logger.log(Level.FINE, "Load font {0}", params);
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
        try (InputStream is = vfs.open(fontName)) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            f = f.deriveFont(fontStyle, fontSize);
        } catch (Exception e) {
            f = new Font("monospaced", fontStyle, fontSize);
        }
        LwjglNuitFont font = new FontAsset(f, antialising, new char[0], new HashMap<Character, BufferedImage>());
        font.setCorrection(false);
        return font;
    }

    private NanimationCollection loadJsonNanim(String name) {
        NanimationCollection nanim = new NanimationCollection();
        try (InputStream is = vfs.open(name); InputStreamReader reader = new InputStreamReader(is)) {
            String nanimParentDir;
            final int lastIndexOfSlash = name.lastIndexOf("/");
            if (lastIndexOfSlash < 0) {
                nanimParentDir = "";
            } else {
                nanimParentDir = name.substring(0, name.lastIndexOf("/") + 1);
            }
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            for (JsonElement jsonAnimationElement : json.getAsJsonArray("animations")) {
                JsonObject jsonAnimation = jsonAnimationElement.getAsJsonObject();
                Nanimation animation = new Nanimation(jsonAnimation.get("name").getAsString());
                for (JsonElement jsonFrameElement : jsonAnimation.getAsJsonArray("frames")) {
                    JsonObject jsonFrame = jsonFrameElement.getAsJsonObject();
                    final String imageFilename = nanimParentDir + jsonFrame.get("image").getAsString();
                    Texture texture = this.loadTexture(imageFilename);
                    NanimationImage image = new NanimationImage(texture);
                    animation.addFrame(image, jsonFrame.get("duration").getAsInt(), jsonFrame.get("u1").getAsFloat(),
                            jsonFrame.get("v1").getAsFloat(), jsonFrame.get("u2").getAsFloat(),
                            jsonFrame.get("v2").getAsFloat());
                }
                nanim.addAnimation(animation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load animation " + name, e);
        }
        return nanim;
    }

    public void setIcon(long glfwWindow, String name) {
        try (InputStream is = vfs.open(name)) {
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
