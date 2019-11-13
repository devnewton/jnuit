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
package im.bci.jnuit.teavm;

import im.bci.jnuit.NuitControls;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.controls.NullControl;
import im.bci.jnuit.controls.Pointer;
import im.bci.jnuit.teavm.controls.Keyboard;
import im.bci.jnuit.teavm.controls.Mouse;
import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.browser.Window;

/**
 *
 * @author devnewton
 */
public class TeavmNuitControls implements NuitControls {

    private Keyboard keyboard = new Keyboard();
    private Mouse mouse = new Mouse();

	public TeavmNuitControls() {
	}

	@Override
	public Control[] getPossibleControls() {
		List<Control> possibleControls = new ArrayList<Control>();
		possibleControls.addAll(keyboard.getPossibleControls());
		possibleControls.addAll(mouse.getPossibleControls());
		return possibleControls.toArray(new Control[possibleControls.size()]);
	}

	@Override
	public Control[] getDefaultMenuUpControls() {
		Control[] controls = new Control[2];
		controls[0] = keyboard.getKeyControl("ArrowUp");
		controls[1] = NullControl.INSTANCE;
		return controls;
	}

	@Override
	public Control[] getDefaultMenuDownControls() {
		Control[] controls = new Control[2];
		controls[0] = keyboard.getKeyControl("ArrowDown");
		controls[1] = NullControl.INSTANCE;
		return controls;
	}

	@Override
	public Control[] getDefaultMenuLeftControls() {
		Control[] controls = new Control[2];
		controls[0] = keyboard.getKeyControl("ArrowLeft");
		controls[1] = NullControl.INSTANCE;
		return controls;
	}

	@Override
	public Control[] getDefaultMenuRightControls() {
		Control[] controls = new Control[2];
		controls[0] = keyboard.getKeyControl("ArrowRight");
		return controls;
	}

	@Override
	public Control[] getDefaultMenuOkControls() {
		Control[] controls = new Control[2];
		controls[0] = keyboard.getKeyControl("Enter");
		controls[1] = NullControl.INSTANCE;
		return controls;
	}

	@Override
	public Control[] getDefaultMenuCancelControls() {
		Control[] controls = new Control[2];
		controls[0] = keyboard.getKeyControl("Escape");
		controls[1] = NullControl.INSTANCE;
		return controls;
	}

	@Override
	public void pollPointer(float virtualResolutionWidth, float virtualResolutionHeight, Pointer pointer) {
		double xpos = mouse.getClientX();
		double ypos = mouse.getClientY();
		int width = Window.current().getInnerWidth();
		int height = Window.current().getInnerHeight();
		pointer.setX((float) (xpos * (double) virtualResolutionWidth / (double) width));
		pointer.setY((float) (ypos * (double) virtualResolutionHeight / (double) height));
		pointer.setDown(mouse.isPressed());
	}

}
