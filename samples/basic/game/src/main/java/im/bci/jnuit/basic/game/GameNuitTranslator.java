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
package im.bci.jnuit.basic.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitLocale;
import im.bci.jnuit.NuitTranslator;
import java.util.Locale;

/**
 *
 * @author devnewton
 */
@Singleton
public class GameNuitTranslator extends NuitTranslator {

    @Inject
    public GameNuitTranslator() {
        if (Locale.getDefault().getLanguage().equals(new Locale("fr").getLanguage())) {
            setCurrentLocale(NuitLocale.FRENCH);
        }

        addEnglish();
        addFrench();
    }

    private void addEnglish() {
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.start", "START");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.resume", "RESUME");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.options", "OPTIONS");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.extras", "EXTRAS");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.quit", "QUIT");

        addTranslation(NuitLocale.ENGLISH, "options.menu.button.video", "VIDEO");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.audio", "AUDIO");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.menu.language", "LANGUAGE");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.game.controls", "GAME CONTROLS");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.menu.controls", "MENU CONTROLS");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.back", "BACK");

        addTranslation(NuitLocale.ENGLISH, "extras.menu.button.cutscenes", "CUTSCENES");
        addTranslation(NuitLocale.ENGLISH, "extras.menu.button.artwork", "ARTWORK");
        addTranslation(NuitLocale.ENGLISH, "extras.menu.button.back", "BACK");

        addTranslation(NuitLocale.ENGLISH, "cutscenes.menu.button.intro", "INTRODUCTION");
        addTranslation(NuitLocale.ENGLISH, "cutscenes.menu.button.credits", "CREDITS");
        addTranslation(NuitLocale.ENGLISH, "cutscenes.menu.button.back", "BACK");

        addTranslation(NuitLocale.ENGLISH, "dialog.button.next", "NEXT");
        addTranslation(NuitLocale.ENGLISH, "dialog.button.previous", "PREVIOUS");

        addTranslation(NuitLocale.ENGLISH, "action.up", "Up");
        addTranslation(NuitLocale.ENGLISH, "action.down", "Down");
        addTranslation(NuitLocale.ENGLISH, "action.left", "Left");
        addTranslation(NuitLocale.ENGLISH, "action.right", "Right");
        addTranslation(NuitLocale.ENGLISH, "action.menu", "Menu");

        addTranslation(NuitLocale.ENGLISH, "dialog.credits.devnewton", "http://devnewton.bci.im");
        
        addTranslation(NuitLocale.FRENCH, "dialog.artworks.01", "Sketch 01");
        addTranslation(NuitLocale.FRENCH, "dialog.artworks.02", "Sketch 02");

    }

    private void addFrench() {
        addTranslation(NuitLocale.FRENCH, "main.menu.button.start", "DEMARRER");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.resume", "CONTINUER");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.options", "OPTIONS");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.extras", "EXTRAS");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.quit", "QUITTER");

        addTranslation(NuitLocale.FRENCH, "options.menu.button.video", "VIDEO");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.audio", "AUDIO");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.menu.language", "LANGUE");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.game.controls", "CONTROLES DU JEU");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.menu.controls", "CONTROLES DES MENUS");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.back", "RETOUR");

        addTranslation(NuitLocale.FRENCH, "extras.menu.button.cutscenes", "CINEMATIQUES");
        addTranslation(NuitLocale.FRENCH, "extras.menu.button.artwork", "ILLUSTRATIONS");
        addTranslation(NuitLocale.FRENCH, "extras.menu.button.back", "RETOUR");

        addTranslation(NuitLocale.FRENCH, "cutscenes.menu.button.credits", "CREDITS");
        addTranslation(NuitLocale.FRENCH, "cutscenes.menu.button.back", "RETOUR");

        addTranslation(NuitLocale.FRENCH, "dialog.button.next", "SUIVANT");
        addTranslation(NuitLocale.FRENCH, "dialog.button.previous", "PRECEDENT");

        addTranslation(NuitLocale.FRENCH, "action.up", "Haut");
        addTranslation(NuitLocale.FRENCH, "action.down", "Bas");
        addTranslation(NuitLocale.FRENCH, "action.left", "Gauche");
        addTranslation(NuitLocale.FRENCH, "action.right", "Droite");
        addTranslation(NuitLocale.FRENCH, "action.menu", "Menu");

        addTranslation(NuitLocale.FRENCH, "dialog.credits.devnewton", "http://devnewton.bci.im");
        
        addTranslation(NuitLocale.FRENCH, "dialog.artworks.01", "Croquis 01");
        addTranslation(NuitLocale.FRENCH, "dialog.artworks.02", "Croquis 02");
    }
}
