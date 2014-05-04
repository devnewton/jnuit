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
package im.bci.jnuit.playn;

import im.bci.jnuit.NuitRenderer;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.ColoredBackground;
import im.bci.jnuit.background.NullBackground;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.border.ColoredBorder;
import im.bci.jnuit.border.NullBorder;
import im.bci.jnuit.focus.ColoredRectangleFocusCursor;
import im.bci.jnuit.focus.FocusCursor;
import im.bci.jnuit.focus.NullFocusCursor;
import im.bci.jnuit.text.TextColor;
import im.bci.jnuit.visitors.BackgroundVisitor;
import im.bci.jnuit.visitors.BorderVisitor;
import im.bci.jnuit.visitors.FocusCursorVisitor;
import im.bci.jnuit.visitors.WidgetVisitor;
import im.bci.jnuit.widgets.AudioConfigurator;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.ControlsConfigurator;
import im.bci.jnuit.widgets.Label;
import im.bci.jnuit.widgets.NullWidget;
import im.bci.jnuit.widgets.Root;
import im.bci.jnuit.widgets.Select;
import im.bci.jnuit.widgets.Stack;
import im.bci.jnuit.widgets.Table;
import im.bci.jnuit.widgets.Toggle;
import im.bci.jnuit.widgets.VideoConfigurator;
import im.bci.jnuit.widgets.Widget;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Image;
import playn.core.Surface;

/**
 *
 * @author devnewton
 */
public class PlaynNuitRenderer implements WidgetVisitor, BackgroundVisitor, NuitRenderer {

    private Surface surface;
    private final NuitTranslator translator;
    private final TopBorderRenderer topBorderRenderer = new TopBorderRenderer();
    private final BottomBorderRenderer bottomBorderRenderer = new BottomBorderRenderer();
    private final LeftBorderRenderer leftBorderRenderer = new LeftBorderRenderer();
    private final RightBorderRenderer rightBorderRenderer = new RightBorderRenderer();
    private final PlaynTextCache textCache;
    private TextColor textColor;

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    private class TopBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            surface.setFillColor(Color.argb((int) (255 * border.getAlpha()), (int) (255 * border.getRed()), (int) (255 * border.getGreen()), (int) (255 * border.getBlue())));
            surface.drawLine(widget.getX(), widget.getY(), widget.getX() + widget.getWidth(), widget.getY(), border.getSize());
            surface.setFillColor(Color.argb(255, 255, 255, 255));
        }

    }

    private class BottomBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            surface.setFillColor(Color.argb((int) (255 * border.getAlpha()), (int) (255 * border.getRed()), (int) (255 * border.getGreen()), (int) (255 * border.getBlue())));
            surface.drawLine(widget.getX(), widget.getY() + widget.getHeight(), widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(), border.getSize());
            surface.setFillColor(Color.argb(255, 255, 255, 255));
        }

    }

    private class LeftBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            surface.setFillColor(Color.argb((int) (255 * border.getAlpha()), (int) (255 * border.getRed()), (int) (255 * border.getGreen()), (int) (255 * border.getBlue())));
            surface.drawLine(widget.getX(), widget.getY(), widget.getX(), widget.getY() + widget.getHeight(), border.getSize());
            surface.setFillColor(Color.argb(255, 255, 255, 255));
        }

    }

    private class RightBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            surface.setFillColor(Color.argb((int) (255 * border.getAlpha()), (int) (255 * border.getRed()), (int) (255 * border.getGreen()), (int) (255 * border.getBlue())));
            surface.drawLine(widget.getX() + widget.getWidth(), widget.getY(), widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(), border.getSize());
            surface.setFillColor(Color.argb(255, 255, 255, 255));
        }

    }

    public PlaynNuitRenderer(NuitTranslator translator, PlaynNuitFont font) {
        this.translator = translator;
        this.textCache = new PlaynTextCache(font);
    }

    @Override
    public void render(Root root) {
        surface.save();
        surface.translate(root.getX(), root.getY());
        surface.scale(surface.width() / root.getWidth(), surface.height() / root.getHeight());
        drawBackgroundAndBorder(root);
        drawStack(root);
        surface.restore();
    }

    private void drawBackgroundAndBorder(Widget widget) {
        widget.getBackground().accept(widget, this);
        drawBorders(widget);
    }

    private void drawBorders(Widget widget) {
        widget.getTopBorder().accept(widget, topBorderRenderer);
        widget.getBottomBorder().accept(widget, bottomBorderRenderer);
        widget.getLeftBorder().accept(widget, leftBorderRenderer);
        widget.getRightBorder().accept(widget, rightBorderRenderer);
    }

    @Override
    public void visit(Button widget) {
        drawText(widget, translator.getMessage(widget.getText()), widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
    }

    private void drawText(Widget widget, String text, float x, float y) {
        if (!text.isEmpty()) {
            TextColor c;
            if (null == textColor) {
                c = widget.getTextColor();
            } else {
                c = textColor;
            }
            int color = Color.argb((int) (255 * c.getAlpha()), (int) (255 * c.getRed()), (int) (255 * c.getGreen()), (int) (255 * c.getBlue()));
            final CanvasImage textCanvasImage = textCache.getTextCanvasImage(text, color);
            surface.drawImage(textCanvasImage, x - textCanvasImage.width() / 2.0f, y - textCanvasImage.height() / 2.0f);
        }
    }

    @Override
    public void visit(Widget widget, ColoredBackground background) {
        surface.setFillColor(Color.argb((int) (255 * background.getAlpha()), (int) (255 * background.getRed()), (int) (255 * background.getGreen()), (int) (255 * background.getBlue())));
        surface.fillRect(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
        surface.setFillColor(Color.argb(255, 255, 255, 255));
    }

    @Override
    public void visit(Widget widget, NullBackground background) {
    }

    @Override
    public void visit(Widget widget, TexturedBackground background) {
        IAnimationFrame frame = background.getPlay().getCurrentFrame();
        if (null != frame) {
            Image image = (Image) frame.getImage().getId();
            surface.save();
            surface.translate(widget.getX() + widget.getWidth()/2.0f, widget.getY()+widget.getHeight()/2.0f);
            surface.scale(background.isMirrorX() ? -1.0f : 1.0f, background.isMirrorY() ? -1.0f : 1.0f);
            surface.drawImage(image,
                    -widget.getWidth()/2.0f,
                    -widget.getHeight()/2.0f,
                    widget.getWidth(),
                    widget.getHeight(),
                    frame.getU1() * image.width(),
                    frame.getV1() * image.height(),
                    (frame.getU2() - frame.getU1()) * image.width(),
                    (frame.getV2() - frame.getV1()) * image.height());
            surface.restore();
        }
    }

    @Override
    public void visit(Container widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(Table widget) {
        drawContainer(widget);
    }

    private void drawContainer(Container widget) {
        Widget focused = widget.getFocusedChild();
        for (Widget child : widget.getChildren()) {
            child.getBackground().accept(child, this);
            if (focused == child) {
                final Background focusedBackground;
                if (widget.isFocusSucked()) {
                    focusedBackground = child.getSuckedFocusedBackground();
                    textColor = child.getSuckedFocusTextColor();
                } else {
                    focusedBackground = child.getFocusedBackground();
                    textColor = child.getFocusedTextColor();
                }
                if (null != focusedBackground) {
                    focusedBackground.accept(child, this);
                }
            } else {
                textColor = child.getTextColor();
            }
            drawBorders(child);
            child.accept(this);
        }
        if (null != focused) {
            drawFocus(widget, focused);
        }
        textColor = null;
    }

    private class FocusRenderer implements FocusCursorVisitor {

        @Override
        public void visit(Widget focused, ColoredRectangleFocusCursor cursor) {
            final float x = focused.getX();
            final float y = focused.getY();
            final float w = focused.getWidth();
            final float h = focused.getHeight();
            final float lineWidth = 2.0f;
            surface.setFillColor(Color.argb((int) (255 * cursor.getAlpha()), (int) (255 * cursor.getRed()), (int) (255 * cursor.getGreen()), (int) (255 * cursor.getBlue())));
            surface.drawLine(x, y, x + w, y, lineWidth);
            surface.drawLine(x, y + h, x + w, y + h, lineWidth);
            surface.drawLine(x, y, x, y + h, lineWidth);
            surface.drawLine(x + w, y, x + w, y + h, lineWidth);
            surface.setFillColor(Color.argb(255, 255, 255, 255));
        }

        @Override
        public void visit(Widget focused, NullFocusCursor cursor) {
        }

    }

    private final FocusRenderer focusRenderer = new FocusRenderer();

    private void drawFocus(Container container, Widget focused) {
        FocusCursor focusCursor = container.isFocusSucked() ? focused.getSuckedFocusCursor() : focused.getFocusCursor();
        focusCursor.accept(focused, focusRenderer);
    }

    @Override
    public void visit(ControlsConfigurator widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(ControlsConfigurator.ControlConfigurator widget) {
        String text = widget.getText();
        if (null != text) {
            drawText(widget, text, widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
        }
    }

    @Override
    public void visit(Label widget) {
        String translatedText = translator.getMessage(widget.getText());
        drawText(widget, translatedText, widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
    }

    @Override
    public void visit(NullWidget widget) {
        for (Widget child : widget.getChildren()) {
            drawBackgroundAndBorder(child);
            child.accept(this);
        }
    }

    @Override
    public void visit(Select widget) {
        String text = String.valueOf(widget.getSelected());
        drawText(widget, text, widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
    }

    @Override
    public void visit(Stack widget) {
        drawStack(widget);
    }

    private void drawStack(Stack widget) {
        Widget child = widget.getFocusedChild();
        if (null != child) {
            drawBackgroundAndBorder(child);
            child.accept(this);
        }
    }

    @Override
    public void visit(Toggle widget) {
        drawText(widget, widget.getText(), widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
    }

    @Override
    public void visit(AudioConfigurator widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(VideoConfigurator widget) {
        drawContainer(widget);
    }

}
