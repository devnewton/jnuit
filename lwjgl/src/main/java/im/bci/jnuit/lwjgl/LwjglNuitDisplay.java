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
package im.bci.jnuit.lwjgl;

import im.bci.jnuit.NuitDisplay;
import im.bci.jnuit.display.VideoResolution;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

/**
 *
 * @author devnewton
 */
public class LwjglNuitDisplay implements NuitDisplay {

    private long glfwWindow;

    public LwjglNuitDisplay(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    @Override
    public List<VideoResolution> listResolutions() {
        TreeSet<VideoResolution> resolutions = new TreeSet<VideoResolution>();

        for (GLFWVidMode m : GLFW.glfwGetVideoModes(getCurrentMonitor())) {
            resolutions.add(new VideoResolution(m.width(), m.height()));
        }
        return new ArrayList<VideoResolution>(resolutions);
    }

    private long getCurrentMonitor() {
        long monitor = 0;
        if (0 != glfwWindow) {
            monitor = GLFW.glfwGetWindowMonitor(glfwWindow);
        }
        if (0 == monitor) {
            monitor = GLFW.glfwGetPrimaryMonitor();
        }
        return monitor;
    }

    @Override
    public void changeResolution(VideoResolution chosenResolution, boolean fullscreen) {
        GLFW.glfwSetWindowMonitor(glfwWindow, fullscreen ? getCurrentMonitor() : 0, 0, 0, chosenResolution.getWidth(),
                chosenResolution.getHeight(), GLFW.GLFW_DONT_CARE);
    }

    @Override
    public VideoResolution getResolution() {
        GLFWVidMode m = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        return new VideoResolution(m.width(), m.height());
    }

    @Override
    public boolean isFullscreen() {
        return 0 != GLFW.glfwGetWindowMonitor(glfwWindow);
    }

    @Override
    public boolean canChangeResolution() {
        return true;
    }
}
