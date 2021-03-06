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

import im.bci.jnuit.display.VideoResolution;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.visitors.WidgetVisitor;

/**
 * Video settings widget.
 * @author devnewton
 */
public class VideoConfigurator extends Table {

    private final Select<VideoResolution> mode;
    private final Toggle fullscreen;
    private final NuitToolkit toolkit;

    public VideoConfigurator(NuitToolkit toolkit) {
        super(toolkit);
        this.toolkit = toolkit;
        defaults().expand();
        cell(new Label(toolkit, "nuit.video.configurator.mode"));
        mode = new Select<VideoResolution>(toolkit, toolkit.listResolutions());
        cell(mode);
        row();
        cell(new Label(toolkit, "nuit.video.configurator.fullscreen"));
        fullscreen = new Toggle(toolkit);
        cell(fullscreen);
        row();
        cell(new Button(toolkit, "nuit.video.configurator.apply") {
            @Override
            public void onOK() {
                changeVideoSettings();
                VideoConfigurator.this.close();
            }
        }).colspan(2);
        row();
        cell(new Button(toolkit, "nuit.video.configurator.back") {
            @Override
            public void onOK() {
                VideoConfigurator.this.close();
            }
        }).colspan(2);
    }

    protected void changeVideoSettings() {
        toolkit.changeResolution(mode.getSelected(), fullscreen.isEnabled());
    }

    @Override
    public void onShow() {
        mode.setSelected(toolkit.getResolution());
        fullscreen.setEnabled(toolkit.isFullscreen());
    }
    
        @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
