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
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.teavm.TeavmNuitFont;
import im.bci.jnuit.teavm.TeavmNuitDisplay;
import im.bci.jnuit.teavm.TeavmNuitRenderer;
import im.bci.jnuit.teavm.TeavmNuitControls;
import im.bci.jnuit.teavm.assets.TeavmAssets;
import im.bci.jnuit.teavm.assets.TeavmVirtualFileSystem;
import im.bci.jnuit.teavm.audio.TeavmNuitAudio;
import im.bci.jnuit.widgets.Root;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;

/**
 *
 * @author devnewton
 */
public abstract class AbstractSample {

    private TeavmNuitRenderer renderer;
    private NuitToolkit toolkit;
    private Root root;
    private CanvasRenderingContext2D ctx;
    private HTMLCanvasElement canvas;

    public void launch(String[] args) {
        this.canvas = (HTMLCanvasElement) Window.current().getDocument().getElementById("sample-canvas");
        this.ctx = canvas.getContext("2d").cast();
        TeavmNuitFont font = new TeavmNuitFont(ctx);
        NuitTranslator translator = new NuitTranslator();
        this.renderer = new TeavmNuitRenderer(translator, ctx);
        final TeavmVirtualFileSystem vfs = new TeavmVirtualFileSystem("data");
        TeavmAssets assets = new TeavmAssets(vfs);
        TeavmNuitAudio audio = new TeavmNuitAudio(assets);
        this.toolkit = new NuitToolkit(new TeavmNuitDisplay(canvas), new TeavmNuitControls(), translator, font,
                renderer, audio);
        this.root = new Root(toolkit);
        setup(toolkit, root, assets);
        Window.requestAnimationFrame(this::frame);
    }

    public void frame(double timestamp) {
        toolkit.update(root);
        ctx.setTransform((double)canvas.getWidth() / (double)toolkit.getVirtualResolutionWidth(), 0, 0, (double)canvas.getHeight() / (double)toolkit.getVirtualResolutionHeight(), 0, 0);
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        renderer.render(root);
        Window.requestAnimationFrame(this::frame);
    }

    protected abstract void setup(NuitToolkit toolkit, Root root, TeavmAssets assets);

}
