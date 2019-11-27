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
package im.bci.jnuit.teavm;

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

import org.teavm.jso.canvas.CanvasImageSource;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLImageElement;

/**
 *
 * @author devnewton
 */
public class TeavmNuitRenderer implements WidgetVisitor, BackgroundVisitor, NuitRenderer {

    private final NuitTranslator translator;
    private final TeavmNuitFont font;
    private final TopBorderRenderer topBorderRenderer = new TopBorderRenderer();
    private final BottomBorderRenderer bottomBorderRenderer = new BottomBorderRenderer();
    private final LeftBorderRenderer leftBorderRenderer = new LeftBorderRenderer();
    private final RightBorderRenderer rightBorderRenderer = new RightBorderRenderer();
    private TextColor textColor;
    private CanvasRenderingContext2D ctx;

    private class TopBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            ctx.beginPath();
            ctx.setLineWidth(border.getSize());
            ctx.setStrokeStyle(htmlColor(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha()));
            ctx.moveTo(widget.getCenterX(), widget.getY());
            ctx.lineTo(widget.getX() + widget.getWidth(), widget.getY());
            ctx.stroke();

            ctx.setStrokeStyle("#000");
            ctx.setLineWidth(1.0);

            /*
             * GL11.glDisable(GL11.GL_TEXTURE_2D); GL11.glLineWidth(border.getSize());
             * GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(),
             * border.getAlpha()); GL11.glBegin(GL11.GL_LINES);
             * GL11.glVertex2f(widget.getX(), widget.getY()); GL11.glVertex2f(widget.getX()
             * + widget.getWidth(), widget.getY()); GL11.glEnd(); GL11.glColor4f(1f, 1f, 1f,
             * 1f); GL11.glLineWidth(1.0f); GL11.glEnable(GL11.GL_TEXTURE_2D);
             */
        }

    }

    private class BottomBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            ctx.beginPath();
            ctx.setLineWidth(border.getSize());
            ctx.setStrokeStyle(htmlColor(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha()));
            ctx.moveTo(widget.getCenterX(), widget.getY() + widget.getHeight());
            ctx.lineTo(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight());
            ctx.stroke();

            ctx.setStrokeStyle("#000");
            ctx.setLineWidth(1.0);

            /*
             * GL11.glDisable(GL11.GL_TEXTURE_2D); GL11.glLineWidth(border.getSize());
             * GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(),
             * border.getAlpha()); GL11.glBegin(GL11.GL_LINES);
             * GL11.glVertex2f(widget.getX(), widget.getY() + widget.getHeight());
             * GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY() +
             * widget.getHeight()); GL11.glEnd(); GL11.glColor4f(1f, 1f, 1f, 1f);
             * GL11.glLineWidth(1.0f); GL11.glEnable(GL11.GL_TEXTURE_2D);
             */
        }

    }

    private class LeftBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            ctx.beginPath();
            ctx.setLineWidth(border.getSize());
            ctx.setStrokeStyle(htmlColor(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha()));
            ctx.moveTo(widget.getX(), widget.getY());
            ctx.lineTo(widget.getX(), widget.getY() + widget.getHeight());
            ctx.stroke();

            ctx.setStrokeStyle("#000");
            ctx.setLineWidth(1.0);

            /*
             * GL11.glDisable(GL11.GL_TEXTURE_2D); GL11.glLineWidth(border.getSize());
             * GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(),
             * border.getAlpha()); GL11.glBegin(GL11.GL_LINES);
             * GL11.glVertex2f(widget.getX(), widget.getY()); GL11.glVertex2f(widget.getX(),
             * widget.getY() + widget.getHeight()); GL11.glEnd(); GL11.glColor4f(1f, 1f, 1f,
             * 1f); GL11.glLineWidth(1.0f); GL11.glEnable(GL11.GL_TEXTURE_2D);
             */
        }

    }

    private class RightBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            ctx.beginPath();
            ctx.setLineWidth(border.getSize());
            ctx.setStrokeStyle(htmlColor(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha()));
            ctx.moveTo(widget.getX() + widget.getWidth(), widget.getY());
            ctx.lineTo(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight());
            ctx.stroke();

            ctx.setStrokeStyle("#000");
            ctx.setLineWidth(1.0);

            /*
             * GL11.glDisable(GL11.GL_TEXTURE_2D); GL11.glLineWidth(border.getSize());
             * GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(),
             * border.getAlpha()); GL11.glBegin(GL11.GL_LINES);
             * GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY());
             * GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY() +
             * widget.getHeight()); GL11.glEnd(); GL11.glColor4f(1f, 1f, 1f, 1f);
             * GL11.glLineWidth(1.0f); GL11.glEnable(GL11.GL_TEXTURE_2D);
             */
        }

    }

    public TeavmNuitRenderer(NuitTranslator translator, CanvasRenderingContext2D ctx) {
        this.translator = translator;
        this.ctx = ctx;
        this.font = new TeavmNuitFont(ctx);

    }

    @Override
    public void render(Root root) {
        drawBackgroundAndBorder(root);
        drawStack(root);
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
        String translatedText = translator.getMessage(widget.getText());
        drawString(widget, translatedText, widget.getX() + widget.getWidth() / 2.0f,
                widget.getY() + widget.getHeight() / 2.0f);
    }

    @Override
    public void visit(Widget widget, ColoredBackground background) {
        ctx.setFillStyle(
                htmlColor(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha()));
        ctx.fillRect(widget.getX(), widget.getY(), widget.getWidth(),
                widget.getHeight());
        ctx.setFillStyle("#000");
        /*
         * GL11.glDisable(GL11.GL_TEXTURE_2D); GL11.glColor4f(background.getRed(),
         * background.getGreen(), background.getBlue(), background.getAlpha());
         * GL11.glRectf(widget.getX(), widget.getY(), widget.getX() + widget.getWidth(),
         * widget.getY() + widget.getHeight()); GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
         * GL11.glEnable(GL11.GL_TEXTURE_2D);
         */
    }

    @Override
    public void visit(Widget widget, NullBackground background) {
    }

    @Override
    public void visit(Widget widget, TexturedBackground background) {
        IAnimationFrame frame = background.getPlay().getCurrentFrame();
        if (null != frame) {
            float width = widget.getWidth();
            if(background.isMirrorX()) {
                width = -width;
            }
            float height = widget.getHeight();
            if(background.isMirrorY()) {
                height = -height;
            }
            CanvasImageSource imageSource = (CanvasImageSource) frame.getImage().getId();
            HTMLImageElement img = imageSource.cast();
            final int imageWidth = img.getWidth();
            final int imageHeight = img.getHeight();
            final float u1 = frame.getU1();
            final float v1 = frame.getV1();
            final float u2 = frame.getU2();
            final float v2 = frame.getV2();
            ctx.drawImage(imageSource, u1 * imageWidth, v1 * imageHeight, (u2 - u1) * imageWidth, (v2 - v1) * imageHeight, widget.getX(), widget.getY(), width, height);
            /*GL11.glBindTexture(GL11.GL_TEXTURE_2D, (Integer) frame.getImage().getId());
            float x1 = widget.getX();
            float x2 = widget.getX() + widget.getWidth();
            float y1 = widget.getY();
            float y2 = widget.getY() + widget.getHeight();
            float u1 = background.isMirrorX() ? frame.getU2() : frame.getU1();
            float v1 = background.isMirrorY() ? frame.getV2() : frame.getV1();
            float u2 = background.isMirrorX() ? frame.getU1() : frame.getU2();
            float v2 = background.isMirrorY() ? frame.getV1() : frame.getV2();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(u1, v2);
            GL11.glVertex2f(x1, y2);
            GL11.glTexCoord2f(u2, v2);
            GL11.glVertex2f(x2, y2);
            GL11.glTexCoord2f(u2, v1);
            GL11.glVertex2f(x2, y1);
            GL11.glTexCoord2f(u1, v1);
            GL11.glVertex2f(x1, y1);
            GL11.glEnd();*/
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
            ctx.setStrokeStyle(htmlColor(cursor.getRed(), cursor.getGreen(), cursor.getBlue(), cursor.getAlpha()));
            ctx.setLineWidth(2.0);
            ctx.strokeRect(focused.getX(), focused.getY(), focused.getWidth(), focused.getHeight());
            ctx.setStrokeStyle("#000");
            ctx.setLineWidth(1.0);
            /*
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(cursor.getRed(), cursor.getGreen(), cursor.getBlue(), cursor.getAlpha());
            GL11.glLineWidth(2.0f);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2f(focused.getX(), focused.getY());
            GL11.glVertex2f(focused.getX() + focused.getWidth(), focused.getY());
            GL11.glVertex2f(focused.getX() + focused.getWidth(), focused.getY() + focused.getHeight());
            GL11.glVertex2f(focused.getX(), focused.getY() + focused.getHeight());
            GL11.glVertex2f(focused.getX(), focused.getY());
            GL11.glEnd();
            GL11.glColor3f(1, 1, 1);
            GL11.glLineWidth(1.0f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            */
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
            drawString(widget, text, widget.getX() + widget.getWidth() / 2.0f,
                    widget.getY() + widget.getHeight() / 2.0f);
        }
    }

    private void drawString(Widget widget, String text, double x, double y) {
        TextColor c;
        if (null == textColor) {
            c = widget.getTextColor();
        } else {
            c = textColor;
        }
        ctx.setFont(font.getCss());
        ctx.setTextBaseline("middle");
        ctx.setTextAlign("center");
        ctx.setFillStyle(htmlColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
        ctx.fillText(text, x, y);
        ctx.setFont("10px sans-serif");
        ctx.setFillStyle("#000");
        ctx.setTextBaseline("alphabetic");
        ctx.setTextAlign("start");
        /*
         * GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
         * font.drawString(text, align); GL11.glColor3f(1f, 1f, 1f);
         */
    }

    @Override
    public void visit(Label widget) {
        String translatedText = translator.getMessage(widget.getText());
        drawString(widget, translatedText, widget.getX() + widget.getWidth() / 2.0f,
                widget.getY() + widget.getHeight() / 2.0f);
    }

    @Override
    public void visit(NullWidget widget) {
        for (Widget child : widget.getChildren()) {
            drawBackgroundAndBorder(child);
            child.accept(this);
        }
    }

    @Override
    public void visit(Select<?> widget) {
        String text = widget.getValuePrefix() + String.valueOf(widget.getSelected()) + widget.getValueSuffix();
        drawString(widget, text, widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
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
        String text = widget.getText();
        drawString(widget, text, widget.getX() + widget.getWidth() / 2.0f, widget.getY() + widget.getHeight() / 2.0f);
    }

    @Override
    public void visit(AudioConfigurator widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(VideoConfigurator widget) {
        drawContainer(widget);
    }

    private static String htmlColor(float r, float g, float b, float a) {
        return String.format("#%02x%02x%02x%02x", (int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255));
    }

}
