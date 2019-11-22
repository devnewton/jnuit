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
package im.bci.jnuit.teavm.samples;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.background.ColoredBackground;
import im.bci.jnuit.teavm.assets.TeavmAssets;
import im.bci.jnuit.widgets.Label;
import im.bci.jnuit.widgets.Root;

/**
 *
 * @author devnewton
 */
public class HelloWorld extends AbstractSample {

    @Override
    protected void setup(NuitToolkit toolkit, Root root, TeavmAssets assets) {
        Label hello = new Label(toolkit, "Hello world!");
        hello.setBackground(new ColoredBackground(0, 0, 0, 1f));
        //hello.setBackground(new TexturedBackground(assets.getAnimations("menu.png").getFirst().start(PlayMode.LOOP)));
        root.show(hello);        
    }
    
    public static void main(String[] args) {
        HelloWorld sample = new HelloWorld();
        sample.launch(args);
    }

}
