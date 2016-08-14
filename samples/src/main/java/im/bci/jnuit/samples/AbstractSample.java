/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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
package im.bci.jnuit.samples;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.lwjgl.LwjglNuitControls;
import im.bci.jnuit.lwjgl.LwjglNuitDisplay;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.lwjgl.LwjglNuitRenderer;
import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import im.bci.jnuit.lwjgl.audio.OpenALNuitAudio;
import im.bci.jnuit.widgets.Root;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public abstract class AbstractSample {

    private static final Logger logger = Logger.getLogger(AbstractSample.class.getName());

    private static void setupLibraryPath() {
        String libraryPath = System.getProperty("java.library.path");
        if (libraryPath != null && libraryPath.contains("natives")) {
            return;
        }
        try {
            File nativeDir = new File(getApplicationDir(), "natives");
            if (nativeDir.exists() && nativeDir.isDirectory() && nativeDir.list().length > 0) {
                String nativePath = nativeDir.getCanonicalPath();
                System.setProperty("org.lwjgl.librarypath", nativePath);
                System.setProperty("net.java.games.input.librarypath", nativePath);
                return;
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "error", e);
        }
        logger.log(Level.INFO, "Cannot find 'natives' library folder, try system libraries");
    }

    public static File getApplicationDir() {
        try {
            return new File(AbstractSample.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException uriEx) {
            logger.log(Level.WARNING, "Cannot find application directory, try current", uriEx);
            return new File(".");
        }
    }

    public void launch(String[] args) {
        try {
            setupLibraryPath();
        } catch (Throwable e) {
            handleError(e, "Unexpected error during startup. Check your java version and your opengl driver.\n");
            return;
        }

        try {
            Display.setFullscreen(false);
            DisplayMode mode = new DisplayMode(640, 480);
            Display.setDisplayMode(mode);
            Display.create();
            Display.setTitle(getClass().getSimpleName());
            LwjglNuitFont font = new LwjglNuitFont(new Font("Arial", Font.BOLD, 32), true, new char[0], new HashMap<Character, BufferedImage>());
            NuitTranslator translator = new NuitTranslator();
            final LwjglNuitRenderer renderer = new LwjglNuitRenderer(translator, font);
            NuitToolkit toolkit = new NuitToolkit(new LwjglNuitDisplay(), new LwjglNuitControls(), translator, font, renderer, new OpenALNuitAudio(createVFS()));
            Root root = new Root(toolkit);
            setup(toolkit, root);
            while (!Display.isCloseRequested()) {
                toolkit.update(root);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                renderer.render(root);
                Display.update(false);
                Display.sync(60);
                Display.processMessages();
                Mouse.poll();
                Keyboard.poll();
                Controllers.poll();
            }
        } catch (Throwable e) {
            handleError(e, "Unexpected error during execution\n");
        }
    }

    public static void handleError(Throwable e, final String defaultMessage) {
        logger.log(Level.SEVERE, defaultMessage, e);
        JOptionPane.showMessageDialog(null, defaultMessage + "\n" + e.getMessage() + (e.getCause() != null ? "\nCause: " + e.getCause().getMessage() : ""), "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected abstract void setup(NuitToolkit toolkit, Root root);

    private VirtualFileSystem createVFS() throws URISyntaxException {
        for (File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()); null != file; file = file.getParentFile()) {
            File dataDir = new File(file, "data");
            if (dataDir.exists()) {
                return new VirtualFileSystem(dataDir);
            }
        }
        throw new RuntimeException("Cannot find data dir");

    }

}
