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
package im.bci.jnuit.basicgame.game.components.ui;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.widgets.Button;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import im.bci.jnuit.basicgame.game.Game;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.basicgame.game.components.Triggerable;
import im.bci.jnuit.basicgame.game.events.ShowMenuTrigger;
import im.bci.jnuit.basicgame.game.events.StartGameTrigger;
import im.bci.jnuit.widgets.Table;

/**
 *
 * @author devnewton
 */
@Singleton
public class LevelSelector extends Table {

    private final Game game;
    private final Provider<StartGameTrigger> startGameTrigger;
    private final Provider<ShowMenuTrigger> showMenuTrigger;

    @Inject
    public LevelSelector(Game game, NuitToolkit toolkit, final IAssets assets, Provider<StartGameTrigger> startGameTrigger, Provider<ShowMenuTrigger> showMenuTrigger) {
        super(toolkit);
        this.game = game;
        this.startGameTrigger = startGameTrigger;
        this.showMenuTrigger = showMenuTrigger;
        for(int i=0; i<10; ++i) {
            LevelSelectorButton levelButton = new LevelSelectorButton(toolkit, String.format("Level %02d", i),  String.format("level%02d.tmx", i));
            this.cell(levelButton);
        }

        Button backButton = new Button(toolkit, "options.menu.button.back") {

            @Override
            public void onOK() {
                LevelSelector.this.onCancel();
            }

        };
        this.cell(backButton);
    }

    private class LevelSelectorButton extends Button {

        private final String levelName;

        public LevelSelectorButton(NuitToolkit toolkit, String text, String levelName) {
            super(toolkit, text);
            this.levelName = levelName;
        }

        @Override
        public void onOK() {
            onStartGame(levelName);
        }
    }

    private void onStartGame(String levelName) {
        game.addEntity(game.createEntity().addComponent(new Triggerable(startGameTrigger.get().withLevelName(levelName))));
    }

    @Override
    public void onCancel() {
        game.addEntity(game.createEntity().addComponent(new Triggerable(showMenuTrigger.get())));
    }

}
