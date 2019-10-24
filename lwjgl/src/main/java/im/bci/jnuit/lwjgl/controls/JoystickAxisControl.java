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

import java.nio.FloatBuffer;
import java.util.Objects;

import org.lwjgl.glfw.GLFW;

public class JoystickAxisControl implements Control {

    private final int pad;
    private final int axis;
    private final float scale;

    public JoystickAxisControl(int pad, int axis, boolean positive) {
        this.pad = pad;
        this.axis = axis;
        this.scale = positive ? 1.0f : -1.0f;
    }

    @Override
    public String getName() {
        return "Axis " + axis + (scale > 0.0f ? '+' : '-');
    }

    @Override
    public float getDeadZone() {
    	return 0.25f;
    }

    @Override
    public float getValue() {
    	FloatBuffer axes = GLFW.glfwGetJoystickAxes(pad);
    	if(null == axes) {
    		return 0.0f;
    	}
    	float[] axesValues = GLFW.glfwGetJoystickAxes(pad).array();
    	if(axis < axesValues.length) {
            return Math.max(0.0f, axesValues[axis] * scale);
    	}else {
    		return 0.0f;
    	}
    }

    @Override
    public String getControllerName() {
        return GLFW.glfwGetJoystickName(pad);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.pad);
        hash = 29 * hash + this.axis;
        hash = 29 * hash + Float.floatToIntBits(this.scale);
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
        final JoystickAxisControl other = (JoystickAxisControl) obj;
        if (!Objects.equals(this.pad, other.pad)) {
            return false;
        }
        if (this.axis != other.axis) {
            return false;
        }
        return Float.floatToIntBits(this.scale) == Float.floatToIntBits(other.scale);
    }

}
