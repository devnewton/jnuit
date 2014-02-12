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
package im.bci.jnuit.widgets;

import im.bci.jnuit.NuitLocale;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.visitors.WidgetVisitor;
import java.util.Arrays;

/**
 * Audio settings widget.
 *
 * @author devnewton
 */
public class LanguageConfigurator extends Table {

    private final NuitToolkit toolkit;
    protected final Select<NuitLocale> textsLanguage;
    protected final Select<NuitLocale> voicesLanguage;

    public LanguageConfigurator(final NuitToolkit toolkit) {
        super(toolkit);
        this.toolkit = toolkit;
        defaults().expand();
        cell(new Label(toolkit, "nuit.language.configurator.texts"));
        textsLanguage = new Select<NuitLocale>(toolkit, Arrays.asList(NuitLocale.values())) {

            @Override
            public void onLeft() {
                super.onLeft();
                changeTextsLocale(getSelected());
            }

            @Override
            public void onRight() {
                super.onRight();
                changeTextsLocale(getSelected());
            }
        };
        textsLanguage.setSelected(toolkit.getCurrentLocale());
        cell(textsLanguage);
        row();
        cell(new Label(toolkit, "nuit.language.configurator.voices"));
        voicesLanguage = new Select<NuitLocale>(toolkit, Arrays.asList(NuitLocale.values())) {

            @Override
            public void onLeft() {
                super.onLeft();
                changeVoicesLocale(getSelected());
            }

            @Override
            public void onRight() {
                super.onRight();
                changeVoicesLocale(getSelected());
            }

        };
        voicesLanguage.setSelected(toolkit.getCurrentLocale());
        cell(voicesLanguage);
        row();
        cell(new Button(toolkit, "nuit.language.configurator.back") {
            @Override
            public void onOK() {
                LanguageConfigurator.this.close();
            }
        }).colspan(2);
    }

    @Override
    public void onShow() {
        textsLanguage.setSelected(toolkit.getCurrentLocale());
    }

    protected void changeTextsLocale(NuitLocale locale) {
        toolkit.changeLocale(locale);
    }

    protected void changeVoicesLocale(NuitLocale locale) {
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
