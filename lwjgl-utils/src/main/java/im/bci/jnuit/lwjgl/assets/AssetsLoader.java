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
import de.matthiasmann.jpegdecoder.JPEGDecoder;
import de.matthiasmann.jpegdecoder.YUVtoRGB;
import de.matthiasmann.twl.utils.PNGDecoder;
import im.bci.jnuit.lwjgl.IconLoader;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.lwjgl.animation.NanimationCollection;
import im.bci.jnuit.lwjgl.animation.Nanimation;
import im.bci.jnuit.lwjgl.animation.NanimationImage;
import im.bci.jnuit.lwjgl.animation.NanimParser.Nanim;
import im.bci.smjpegdecoder.SmjpegDecoder;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.Scanner;
import org.lwjgl.opengl.GL11;

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
        try {
            logger.log(Level.FINE, "Load texture {0}", name);
            if (name.endsWith("png")) {
                return loadPngTexture(name);
            } else if (name.endsWith("jpg")) {
                return loadJpegTexture(name);
            } else {
                throw new RuntimeException("Unknown texture file format (must be png or jpg): " + name);
            }
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

    public SmjpegDecoder loadVideo(String name) {
        try {
            return new SmjpegDecoder(vfs.openRandomAccess(name));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load video" + name, e);
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

    private Texture loadPngTexture(String name) throws FileNotFoundException, IOException {
        InputStream is = vfs.open(name);
        try {
            PNGDecoder decoder = new PNGDecoder(is);
            int bpp;
            PNGDecoder.Format format;
            int pixelFormat;
            int texWidth = decoder.getWidth();
            int texHeight = decoder.getHeight();
            boolean hasAlpha = decoder.hasAlpha();
            if (hasAlpha) {
                bpp = 4;
                format = PNGDecoder.Format.RGBA;
                pixelFormat = GL11.GL_RGBA;
            } else {
                bpp = 3;
                format = PNGDecoder.Format.RGB;
                pixelFormat = GL11.GL_RGB;
            }

            int stride = bpp * texWidth;
            ByteBuffer buffer = ByteBuffer.allocateDirect(stride * texHeight);
            decoder.decode(buffer, stride, format);
            buffer.flip();
            Texture texture = new Texture(texWidth, texHeight, hasAlpha);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            LwjglHelper.setupGLTextureParams();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelFormat, texWidth,
                    texHeight, 0, pixelFormat, GL11.GL_UNSIGNED_BYTE, buffer);
            return texture;
        } finally {
            is.close();
        }
    }

    public String loadText(String name) {
        try {
            InputStream is = vfs.open(name);
            try {
                Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\Z");
                return s.next();
            } finally {
                is.close();
            }
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Cannot find text file: " + name, ex);
        } catch (IOException ex) {
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
        try {
            InputStream is = vfs.open(fontName);
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            try {
                f = f.deriveFont(fontStyle, fontSize);
            } finally {
                is.close();
            }
        } catch (Exception e) {
            f = new Font("monospaced", fontStyle, fontSize);
        }
        LwjglNuitFont font = new FontAsset(f, antialising, new char[0], new HashMap<Character, BufferedImage>());
        font.setCorrection(false);
        return font;
    }

    private NanimationCollection loadJsonNanim(String name) {
        NanimationCollection nanim = new NanimationCollection();
        try {
            InputStream is = vfs.open(name);
            try {
                InputStreamReader reader = new InputStreamReader(is);
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
                        animation.addFrame(image, jsonFrame.get("duration").getAsInt(), jsonFrame.get("u1").getAsFloat(), jsonFrame.get("v1").getAsFloat(), jsonFrame.get("u2").getAsFloat(), jsonFrame.get("v2").getAsFloat());
                    }
                    nanim.addAnimation(animation);
                }
            } finally {
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load animation " + name, e);
        }
        return nanim;
    }

    public void setIcon(long glfwWindow, String name) {
        try {
            InputStream is = vfs.open(name);
            try {
                IconLoader.setIcon(glfwWindow, is);
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(AssetsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Texture loadJpegTexture(String name) throws IOException {
        InputStream is = vfs.open(name);
        try {
            JPEGDecoder decoder = new JPEGDecoder(is);
            decoder.startDecode();
            int texWidth = decoder.getImageWidth();
            int texHeight = decoder.getImageHeight();
            int stride = 3 * texWidth;
            ByteBuffer buffer = ByteBuffer.allocateDirect(stride * texHeight);
            decoder.decode(buffer, stride, decoder.getNumMCURows(), YUVtoRGB.instance);
            buffer.flip();
            Texture texture = new Texture(texWidth, texHeight, false);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            LwjglHelper.setupGLTextureParams();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, texWidth, texHeight, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
            return texture;
        } finally {
            is.close();
        }
    }

    private static class FontAsset extends LwjglNuitFont {

        public FontAsset(Font font, boolean antiAlias, char[] additionalChars, Map<Character, BufferedImage> specialCharacters) {
            super(font, antiAlias, additionalChars, specialCharacters);
        }

        @Override
        public void deleteFontTexture() {
            //NOTHING
        }
    }
}
