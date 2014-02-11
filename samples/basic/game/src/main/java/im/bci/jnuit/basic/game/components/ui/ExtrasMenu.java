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
package im.bci.jnuit.basic.game.components.ui;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Stack;

/**
 *
 * @author devnewton
 */
public class ExtrasMenu extends Stack {
    
        private final Container menu;
    
    public ExtrasMenu(final NuitToolkit toolkit, final IAssets assets, final CutScenes cutscenes) {
        
        final CutScenesMenu cutscenesMenu = new CutScenesMenu(toolkit, assets, cutscenes);
        
        menu = new Container();
        
        final Button cutscenesButton = new Button(toolkit, "extras.menu.button.cutscenes") {
            @Override
            public void onOK() {
                ExtrasMenu.this.show(cutscenesMenu);
            }
        };
        cutscenesButton.setX(110);
        cutscenesButton.setY(400);
        cutscenesButton.setWidth(400);
        cutscenesButton.setHeight(80);
        menu.add(cutscenesButton);

        final Button artworkButton = new Button(toolkit, "extras.menu.button.artwork") {
            @Override
            public void onOK() {
                Dialog artwork = new Dialog(toolkit, assets);
                cutscenes.createArtworks(artwork);
                artwork.setWidth(ExtrasMenu.this.getWidth());
                artwork.setHeight(ExtrasMenu.this.getHeight());
                ExtrasMenu.this.show(artwork);
            }
        };
        artworkButton.setX(110);
        artworkButton.setY(500);
        artworkButton.setWidth(400);
        artworkButton.setHeight(80);
        menu.add(artworkButton);

        final Button backButton = new Button(toolkit, "extras.menu.button.back") {
            @Override
            public void onOK() {
                ExtrasMenu.this.close();
            }
        };
        backButton.setX(110);
        backButton.setY(600);
        backButton.setWidth(400);
        backButton.setHeight(80);
        menu.add(backButton);
        
        this.show(menu);
    }

}
