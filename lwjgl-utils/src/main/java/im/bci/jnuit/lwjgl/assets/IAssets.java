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

import im.bci.jnuit.animation.ITexture;
import im.bci.jnuit.NuitFont;
import im.bci.jnuit.animation.IAnimationCollection;

/**
 *
 * @author devnewton
 */
public interface IAssets {

    void clearAll();

    void clearUseless();
    
    void forceAnimationUnload(String name);

    IAnimationCollection getAnimations(String name);
    
    IAnimationCollection getAnimationFromSubTexture(String name, float u1, float v1, float u2, float v2);

    NuitFont getFont(String name);

    ITexture getTexture(String name);

    TmxAsset getTmx(String name);

    ITexture grabScreenToTexture();

    void setIcon(long glfwWindow, String name);

    String getText(String name);
    
}
