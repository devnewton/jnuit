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
package im.bci.jnuit.lwjgl.controls;

import im.bci.jnuit.controls.Control;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.lwjgl.glfw.GLFW;

public class JoystickButtonControl implements Control {

    private final int pad;
    private final int button;

    public JoystickButtonControl(int pad, int button) {
        this.pad = pad;
        this.button = button;
    }

    @Override
    public String getName() {
        return "Button " + button;
    }

    @Override
    public float getDeadZone() {
        return 0.1f;
    }

    @Override
    public float getValue() {
    	ByteBuffer buttons = GLFW.glfwGetJoystickButtons(pad);
    	if(null == buttons) {
    		return 0f;
    	}
    	byte[] buttonsValues = buttons.array();
    	if(button < buttonsValues.length) {
    		return buttonsValues[button] == GLFW.GLFW_PRESS ? 1.0f : 0.0f;
    	} else {
    		return 0f;
    	}
    }

    @Override
    public String getControllerName() {
    	return GLFW.glfwGetJoystickName(pad);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.pad);
        hash = 37 * hash + this.button;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JoystickButtonControl other = (JoystickButtonControl) obj;
        if (!Objects.equals(this.pad, other.pad)) {
            return false;
        }
        return this.button == other.button;
    }

}
