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
package im.bci.jnuit.playn;

import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.controls.Control;
import playn.core.PlayN;

/**
 *
 * @author devnewton
 */
public class PlaynNuitPreferences implements NuitPreferences {

    private final NuitControls controls;
    
    public PlaynNuitPreferences(NuitControls controls, String appName) {
        this.controls = controls;
    }

    @Override
    public void saveConfig() {
    }

    @Override
    public void putBoolean(String name, boolean value) {
        PlayN.storage().setItem(name, String.valueOf(value));
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return Boolean.valueOf(getSystemOrStoreProperty(name, String.valueOf(defaultValue)));
    }

    @Override
    public void putInt(String name, int value) {
        PlayN.storage().setItem(name, String.valueOf(value));
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return Integer.valueOf(getSystemOrStoreProperty(name, String.valueOf(defaultValue)));
    }

    @Override
    public void putFloat(String name, float value) {
        PlayN.storage().setItem(name, String.valueOf(value));
    }

    @Override
    public float getFloat(String name, float defaultValue) {
        return Float.valueOf(getSystemOrStoreProperty(name, String.valueOf(defaultValue)));
    }

    @Override
    public void putControl(String name, Control value) {
        if (null != value) {
            PlayN.storage().setItem(name + ".controller", value.getControllerName());
            PlayN.storage().setItem(name + ".control", value.getName());
        } else {
            PlayN.storage().removeItem(name + ".controller");
            PlayN.storage().removeItem(name + ".control");
        }
    }

    @Override
    public Control getControl(String name, Control defaultValue) {
        String controllerName = getSystemOrStoreProperty(name + ".controller", null);
        String controlName = getSystemOrStoreProperty(name + ".control", null);
        for (Control control : controls.getPossibleControls()) {
            if (control.getControllerName().equals(controllerName) && control.getName().equals(controlName)) {
                return control;
            }
        }
        return defaultValue;
    }

    private String getSystemOrStoreProperty(String name, String defaultValue) {
        String value = PlayN.storage().getItem(name);
        if (null != value) {
            return value;
        } else {
            return defaultValue;
        }
    }

    @Override
    public String getString(String name, String defaultValue) {
        return getSystemOrStoreProperty(name, String.valueOf(defaultValue));
    }

    @Override
    public void putString(String name, String value) {
        PlayN.storage().setItem(name, value);
    }

}
