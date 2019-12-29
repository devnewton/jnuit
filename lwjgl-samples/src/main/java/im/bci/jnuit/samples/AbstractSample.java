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
import im.bci.jnuit.lwjgl.assets.AssetsLoader;
import im.bci.jnuit.lwjgl.assets.GarbageCollectedAssets;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import im.bci.jnuit.lwjgl.audio.OpenALNuitAudio;
import im.bci.jnuit.widgets.Root;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public abstract class AbstractSample {

    private static final Logger logger = Logger.getLogger(AbstractSample.class.getName());

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
        	GLFWErrorCallback.createPrint(System.err).set();
        	if (!GLFW.glfwInit()) {
                throw new IllegalStateException("Unable to initialize glfw");
            }
        	GLFW.glfwDefaultWindowHints();
        	GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        	GLFW.glfwWindowHint(GLFW.GLFW_SCALE_TO_MONITOR, GLFW.GLFW_TRUE);
        	GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        	long window = GLFW.glfwCreateWindow(640, 480, getClass().getSimpleName(), 0, 0);
        	if(0 == window) {
        		throw new RuntimeException("Failed to create the GLFW window");
        	}
        	GLFW.glfwMakeContextCurrent(window);
        	GLFW.glfwSwapInterval(1);//enable vsync
        	GLFW.glfwShowWindow(window);
        	GL.createCapabilities();
            LwjglNuitFont font = new LwjglNuitFont(new Font("Arial", Font.BOLD, 32), true, new char[0], new HashMap<Character, BufferedImage>());
            NuitTranslator translator = new NuitTranslator();
            final LwjglNuitRenderer renderer = new LwjglNuitRenderer(translator, font, window);
            final VirtualFileSystem vfs = new VirtualFileSystem();
            GarbageCollectedAssets assets = new GarbageCollectedAssets(vfs);
            OpenALNuitAudio audio = new OpenALNuitAudio(vfs);
			NuitToolkit toolkit = new NuitToolkit(new LwjglNuitDisplay(window), new LwjglNuitControls(window), translator, font, renderer, audio);
            Root root = new Root(toolkit);
            setup(toolkit, assets, root);
            while (!GLFW.glfwWindowShouldClose(window)) {
                toolkit.update(root);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                renderer.render(root);
                GLFW.glfwSwapBuffers(window);
                GLFW.glfwPollEvents();
            }
            assets.clearAll();
            GLFW.glfwDestroyWindow(window);
            GLFW.glfwTerminate();
            audio.close();
        } catch (Throwable e) {
            handleError(e, "Unexpected error during execution\n");
        }
    }

    public static void handleError(Throwable e, final String defaultMessage) {
        logger.log(Level.SEVERE, defaultMessage, e);
        JOptionPane.showMessageDialog(null, defaultMessage + "\n" + e.getMessage() + (e.getCause() != null ? "\nCause: " + e.getCause().getMessage() : ""), "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected abstract void setup(NuitToolkit toolkit, IAssets assets, Root root);

}
