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
package im.bci.jnuit.widgets;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.visitors.WidgetVisitor;

/**
 * Simple label widget.
 * @author devnewton
 */
public class Label extends Widget {

    private String text;
    private final NuitToolkit toolkit;

    public Label(NuitToolkit toolkit, String text) {
        this.toolkit = toolkit;
        this.text = text;
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return text;
    }

    @Override
    public float getMinWidth() {
        return toolkit.getFont().getWidth(toolkit.getMessage(text));
    }

    @Override
    public float getMinHeight() {
        return toolkit.getFont().getHeight(toolkit.getMessage(text));
    }
}
