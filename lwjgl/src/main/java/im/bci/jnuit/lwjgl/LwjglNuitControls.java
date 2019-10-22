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

import im.bci.jnuit.NuitControls;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.controls.NullControl;
import im.bci.jnuit.controls.Pointer;
import im.bci.jnuit.lwjgl.controls.GamepadAxisControl;
import im.bci.jnuit.lwjgl.controls.GamepadButtonControl;
import im.bci.jnuit.lwjgl.controls.KeyControl;
import im.bci.jnuit.lwjgl.controls.MouseButtonControl;
import im.bci.jnuit.widgets.ControlsConfigurator;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.glfw.GLFW;

/**
 *
 * @author devnewton
 */
public class LwjglNuitControls implements NuitControls {

	private final long glfwWindow;

	public LwjglNuitControls(long glfwWindow) {
		this.glfwWindow = glfwWindow;
	}

	@Override
	public Control[] getPossibleControls() {
		List<Control> possibleControls = new ArrayList<Control>();
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad <= GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(pad);
			if (null != axes) {
				int nbAxis = axes.array().length;
				for (int a = 0; a < nbAxis; ++a) {
					possibleControls.add(new GamepadAxisControl(pad, a, true));
					possibleControls.add(new GamepadAxisControl(pad, a, false));
				}
			}
			ByteBuffer buttons = GLFW.glfwGetJoystickButtons(pad);
			if (null != buttons) {
				int nbButtons = buttons.array().length;
				for (int b = 0; b < nbButtons; ++b) {
					possibleControls.add(new GamepadButtonControl(pad, b));
				}
			}
		}
		for (Field field : GLFW.class.getFields()) {
			String name = field.getName();
			if (name.startsWith("GLFW_KEY_")) {
				try {
					int key = field.getInt(null);
					possibleControls.add(new KeyControl(glfwWindow, key));
				} catch (Exception e) {
					Logger.getLogger(ControlsConfigurator.class.getName()).log(Level.SEVERE, "error retrieving key", e);
				}
			}
		}
		for (int b = GLFW.GLFW_MOUSE_BUTTON_1; b <= GLFW.GLFW_MOUSE_BUTTON_LAST; ++b) {
			possibleControls.add(new MouseButtonControl(glfwWindow, b));
		}
		return possibleControls.toArray(new Control[possibleControls.size()]);
	}

	@Override
	public Control[] getDefaultMenuUpControls() {
		Control[] controls = new Control[2];
		controls[0] = new KeyControl(glfwWindow, GLFW.GLFW_KEY_UP);
		controls[1] = NullControl.INSTANCE;
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad < GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(pad);
			if (null != axes && axes.array().length >= 2) {
				controls[1] = new GamepadAxisControl(pad, 1, true);
			}
		}
		return controls;
	}

	@Override
	public Control[] getDefaultMenuDownControls() {
		Control[] controls = new Control[2];
		controls[0] = new KeyControl(glfwWindow, GLFW.GLFW_KEY_DOWN);
		controls[1] = NullControl.INSTANCE;
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad < GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(pad);
			if (null != axes && axes.array().length >= 2) {
				controls[1] = new GamepadAxisControl(pad, 1, false);
			}
		}
		return controls;
	}

	@Override
	public Control[] getDefaultMenuLeftControls() {
		Control[] controls = new Control[2];
		controls[0] = new KeyControl(glfwWindow, GLFW.GLFW_KEY_LEFT);
		controls[1] = NullControl.INSTANCE;
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad < GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(pad);
			if (null != axes && axes.array().length >= 1) {
				controls[1] = new GamepadAxisControl(pad, 0, false);
			}
		}
		return controls;
	}

	@Override
	public Control[] getDefaultMenuRightControls() {
		Control[] controls = new Control[2];
		controls[0] = new KeyControl(glfwWindow, GLFW.GLFW_KEY_RIGHT);
		controls[1] = NullControl.INSTANCE;
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad < GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(pad);
			if (null != axes && axes.array().length >= 1) {
				controls[1] = new GamepadAxisControl(pad, 0, true);
			}
		}
		return controls;
	}

	@Override
	public Control[] getDefaultMenuOkControls() {
		Control[] controls = new Control[2];
		controls[0] = new KeyControl(glfwWindow, GLFW.GLFW_KEY_ENTER);
		controls[1] = NullControl.INSTANCE;
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad < GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			ByteBuffer buttons = GLFW.glfwGetJoystickButtons(pad);
			if (null != buttons && buttons.array().length >= 1) {
				controls[1] = new GamepadButtonControl(pad, 0);
			}
		}
		return controls;
	}

	@Override
	public Control[] getDefaultMenuCancelControls() {
		Control[] controls = new Control[2];
		controls[0] = new KeyControl(glfwWindow, GLFW.GLFW_KEY_ESCAPE);
		controls[1] = NullControl.INSTANCE;
		for (int pad = GLFW.GLFW_JOYSTICK_1; pad < GLFW.GLFW_JOYSTICK_LAST; ++pad) {
			ByteBuffer buttons = GLFW.glfwGetJoystickButtons(pad);
			if (null != buttons && buttons.array().length >= 2) {
				controls[1] = new GamepadButtonControl(pad, 1);
			}
		}
		return controls;
	}

	@Override
	public void pollPointer(float virtualResolutionWidth, float virtualResolutionHeight, Pointer pointer) {
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		GLFW.glfwGetCursorPos(glfwWindow, xpos, ypos);
		int[] width = new int[1];
		int[] height = new int[1];
		GLFW.glfwGetFramebufferSize(glfwWindow, width, height);
		pointer.setX((float) (xpos[0] * (double) virtualResolutionWidth / (double) width[0]));
		pointer.setY((float) (ypos[0] * (double) virtualResolutionHeight / (double) height[0]));
		pointer.setDown(GLFW.glfwGetMouseButton(glfwWindow, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS);
	}

}
