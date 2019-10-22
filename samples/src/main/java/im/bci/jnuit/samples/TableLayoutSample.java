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
package im.bci.jnuit.samples;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.background.ColoredBackground;
import im.bci.jnuit.widgets.Label;
import im.bci.jnuit.widgets.Root;
import im.bci.jnuit.widgets.Table;

/**
 *
 * @author devnewton
 */
public class TableLayoutSample extends AbstractSample {

    @Override
    protected void setup(NuitToolkit toolkit, Root root) {
        Table table = new Table(toolkit);
        table.defaults().expand().fill();

        Label red = new Label(toolkit, "red");
        red.setBackground(new ColoredBackground(1, 0, 0, 1));
        table.cell(red);

        Label green = new Label(toolkit, "green");
        green.setBackground(new ColoredBackground(0, 1, 0, 1));
        table.cell(green);
        
        table.row();

        Label blue = new Label(toolkit, "blue");
        blue.setBackground(new ColoredBackground(0, 0, 1, 1));
        table.cell(blue).colspan(2);
        
        table.layout();

        root.show(table);
    }

    public static void main(String[] args) {
        TableLayoutSample sample = new TableLayoutSample();
        sample.launch(args);
    }

}
