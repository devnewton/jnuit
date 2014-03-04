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
package im.bci.jnuit.playn.controls;

import im.bci.jnuit.NuitControls;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.controls.NullControl;
import im.bci.jnuit.controls.Pointer;
import im.bci.jnuit.widgets.Root;
import java.util.ArrayList;
import java.util.List;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.PlayN;

/**
 *
 * @author devnewton
 */
public class PlaynNuitControls implements NuitControls {
    
    Pointer pointer = new Pointer();
    boolean isMouseButtonRightDown, isMouseButtonMiddleDown;
    boolean[] keysDown = new boolean[Key.values().length];
    
    public PlaynNuitControls() {
        
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            
            @Override
            public void onKeyDown(Keyboard.Event event) {
                keysDown[event.key().ordinal()] = false;
            }
            
            @Override
            public void onKeyUp(Keyboard.Event event) {
                keysDown[event.key().ordinal()] = true;
            }
        });
        
        if (PlayN.mouse().hasMouse()) {
            PlayN.mouse().setListener(new Mouse.Adapter() {
                
                @Override
                public void onMouseDown(Mouse.ButtonEvent be) {
                    final int button = be.button();
                    if (Mouse.BUTTON_LEFT == button) {
                        pointer.setDown(true);
                    } else if (Mouse.BUTTON_RIGHT == be.button()) {
                        isMouseButtonRightDown = true;
                    } else if (Mouse.BUTTON_MIDDLE == be.button()) {
                        isMouseButtonMiddleDown = true;
                    }
                }
                
                @Override
                public void onMouseUp(Mouse.ButtonEvent be) {
                    final int button = be.button();
                    if (Mouse.BUTTON_LEFT == button) {
                        pointer.setDown(false);
                    } else if (Mouse.BUTTON_RIGHT == be.button()) {
                        isMouseButtonRightDown = false;
                    } else if (Mouse.BUTTON_MIDDLE == be.button()) {
                        isMouseButtonMiddleDown = false;
                    }
                }
                
                @Override
                public void onMouseMove(Mouse.MotionEvent me) {
                    pointer.setX(me.x());
                    pointer.setX(me.y());
                }
            });
        } else {
            PlayN.pointer().setListener(new playn.core.Pointer.Listener() {
                
                @Override
                public void onPointerStart(playn.core.Pointer.Event event) {
                    pointer.setX(event.x());
                    pointer.setX(event.y());
                    pointer.setDown(true);
                }
                
                @Override
                public void onPointerEnd(playn.core.Pointer.Event event) {
                    pointer.setX(event.x());
                    pointer.setX(event.y());
                    pointer.setDown(false);
                }
                
                @Override
                public void onPointerDrag(playn.core.Pointer.Event event) {
                    pointer.setX(event.x());
                    pointer.setX(event.y());
                }
                
                @Override
                public void onPointerCancel(playn.core.Pointer.Event event) {
                    pointer.setX(event.x());
                    pointer.setX(event.y());
                    pointer.setDown(false);
                }
            });
        }
    }
    
    @Override
    public List<Control> getPossibleControls() {
        List<Control> possibleControls = new ArrayList<>();
        for (Key key : Key.values()) {
            possibleControls.add(new KeyControl(this, key));
        }
        possibleControls.add(new MouseButtonControl("Left click", Mouse.BUTTON_LEFT) {
            
            @Override
            public float getValue() {
                return pointer.isDown() ? 1.0f : 0.0f;
            }
            
        });
        
        if (PlayN.mouse().hasMouse()) {
            possibleControls.add(new MouseButtonControl("Right click", Mouse.BUTTON_RIGHT) {
                
                @Override
                public float getValue() {
                    return isMouseButtonRightDown ? 1.0f : 0.0f;
                }
                
            });
            possibleControls.add(new MouseButtonControl("Middle click", Mouse.BUTTON_MIDDLE) {
                
                @Override
                public float getValue() {
                    return isMouseButtonMiddleDown ? 1.0f : 0.0f;
                }
                
            });
        }
        return possibleControls;
    }
    
    @Override
    public Control[] getDefaultMenuUpControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(this, Key.UP);
        controls[1] = NullControl.INSTANCE;
        return controls;
    }
    
    @Override
    public Control[] getDefaultMenuDownControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(this, Key.DOWN);
        controls[1] = NullControl.INSTANCE;
        return controls;
    }
    
    @Override
    public Control[] getDefaultMenuLeftControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(this, Key.LEFT);
        controls[1] = NullControl.INSTANCE;
        return controls;
    }
    
    @Override
    public Control[] getDefaultMenuRightControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(this, Key.RIGHT);
        controls[1] = NullControl.INSTANCE;
        return controls;
    }
    
    @Override
    public Control[] getDefaultMenuOkControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(this, Key.ENTER);
        controls[1] = NullControl.INSTANCE;
        return controls;
    }
    
    @Override
    public Control[] getDefaultMenuCancelControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(this, Key.ESCAPE);
        controls[1] = NullControl.INSTANCE;
        return controls;
    }
    
    @Override
    public void pollPointer(Root root, Pointer p) {
        p.setX(pointer.getX() * root.getWidth() / PlayN.graphics().width());
        p.setY(root.getHeight() - (pointer.getY() * root.getHeight() / PlayN.graphics().height()));
        p.setDown(pointer.isDown());
    }
    
}
