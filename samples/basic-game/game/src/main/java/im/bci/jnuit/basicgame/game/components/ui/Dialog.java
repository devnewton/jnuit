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
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Label;
import im.bci.jnuit.animation.IPlay;
import java.util.ArrayList;
import com.google.inject.Inject;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.NullBackground;
import im.bci.jnuit.lwjgl.assets.IAssets;

/**
 *
 * @author devnewton
 */
public class Dialog extends Container {

    private final ArrayList<Sentence> sentences = new ArrayList<>();
    private int currentSentenceIndex;
    private IPlay currentPlay;
    private boolean finished;
    private final Label textLabel;
    private final Label view;
    private final Button nextButton;

    private Sentence getCurrentSentence() {
        if (!sentences.isEmpty()) {
            return sentences.get(currentSentenceIndex);
        } else {
            return null;
        }
    }

    private static class Sentence {

        IPlay play;
        String text;
        int x;
        int y;
        int w;
        int h;

        private Sentence(IPlay play, int x, int y, int w, int h, String text) {
            this.play = play;
            this.text = text;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    @Inject
    public Dialog(NuitToolkit toolkit, IAssets assets) {
        nextButton = new Button(toolkit, "dialog.button.next") {

            @Override
            public void onOK() {
                onNext();
            }

        };
        nextButton.setX(1280 - 166);
        nextButton.setY(800 - 64);
        nextButton.setWidth(166);
        nextButton.setHeight(64);

        Button previousButton = new Button(toolkit, "") {
            @Override
            public void onOK() {
                onPrevious();
            }

            @Override
            public boolean isFocusable() {
                return currentSentenceIndex > 0 && super.isFocusable();
            }

            @Override
            public Background getBackground() {
                if (currentSentenceIndex > 0) {
                    return super.getBackground();
                } else {
                    return NullBackground.INSTANCE;
                }
            }
        };
        previousButton.setX(0);
        previousButton.setY(800 - 64);
        previousButton.setWidth(166);
        previousButton.setHeight(64);

        this.textLabel = new Label(toolkit, "");
        textLabel.setX(166);
        textLabel.setY(800 - 64);
        textLabel.setWidth(1280 - 166 * 2);
        textLabel.setHeight(64);

        view = new Label(toolkit, "");
        view.setWidth(1280);
        view.setHeight(800);

        add(view);
        add(textLabel);
        add(nextButton);
        add(previousButton);
        setFocusedChild(nextButton);
    }

    public void addTirade(IPlay play, int x, int y, int w, int h, String... sentences) {
        for (String sentence : sentences) {
            this.sentences.add(new Sentence(play, x, y, w, h, sentence));
        }
        onChangeSentence();
    }

    public void addTirade(IPlay play, String... sentences) {
        for (String sentence : sentences) {
            this.sentences.add(new Sentence(play, 0, 0, 1280, 800, sentence));
        }
        onChangeSentence();
    }

    protected void onNext() {
        if (currentSentenceIndex < (sentences.size() - 1)) {
            ++currentSentenceIndex;
        } else {
            onFinished();
        }
        onChangeSentence();
    }

    protected void onChangeSentence() {
        Sentence currentSentence = getCurrentSentence();
        if (null != currentSentence) {
            textLabel.setText(currentSentence.text);
            if (currentPlay != currentSentence.play) {
                currentPlay = currentSentence.play;
                currentSentence.play.restart();
                view.setX(currentSentence.x);
                view.setY(currentSentence.y);
                view.setWidth(currentSentence.w);
                view.setHeight(currentSentence.h);
                view.setBackground(new TexturedBackground(currentSentence.play));
            }
        }
        if(currentSentenceIndex == 0) {
            setFocusedChild(nextButton);
        }
    }

    protected void onPrevious() {
        if (currentSentenceIndex > 0) {
            --currentSentenceIndex;
        }
        onChangeSentence();
    }

    public boolean isFinished() {
        return finished;
    }

    protected void onFinished() {
        this.finished = true;
    }
}
