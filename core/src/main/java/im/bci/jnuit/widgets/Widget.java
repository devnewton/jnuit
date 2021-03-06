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

import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.NullBackground;
import im.bci.jnuit.border.Border;
import im.bci.jnuit.border.NullBorder;
import im.bci.jnuit.focus.ColoredRectangleFocusCursor;
import im.bci.jnuit.focus.FocusCursor;
import im.bci.jnuit.text.TextColor;
import im.bci.jnuit.visitors.WidgetVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Base class for all widget.
 *
 * @author devnewton
 */
public abstract class Widget {

    private float x, y, width, height;
    private FocusCursor focusCursor = ColoredRectangleFocusCursor.WHITE;
    private FocusCursor suckedFocusCursor = ColoredRectangleFocusCursor.GREY;
    private Background background = NullBackground.INSTANCE;
    private Background focusedBackground = NullBackground.INSTANCE;
    private Background suckedFocusedBackground = NullBackground.INSTANCE;
    private Border topBorder = NullBorder.INSTANCE, bottomBorder = NullBorder.INSTANCE, leftBorder = NullBorder.INSTANCE, rightBorder = NullBorder.INSTANCE;
    private final List<Widget> children = new ArrayList<Widget>();
    private Widget parent;
    protected TextColor focusedTextColor = TextColor.WHITE;
    protected TextColor suckedFocusTextColor = TextColor.WHITE;
    protected TextColor textColor = TextColor.WHITE;

    public FocusCursor getFocusCursor() {
        return focusCursor;
    }

    public void setFocusCursor(FocusCursor focusCursor) {
        this.focusCursor = focusCursor;
    }

    public FocusCursor getSuckedFocusCursor() {
        return suckedFocusCursor;
    }

    public void setSuckedFocusCursor(FocusCursor suckedFocusCursor) {
        this.suckedFocusCursor = suckedFocusCursor;
    }

    public Background getSuckedFocusedBackground() {
        return suckedFocusedBackground;
    }

    public void setSuckedFocusedBackground(Background suckedFocusedBackground) {
        this.suckedFocusedBackground = suckedFocusedBackground;
    }

    public Widget getParent() {
        return parent;
    }

    public void setParent(Widget parent) {
        this.parent = parent;
    }

    /**
     * Remove widget from parent if any
     */
    public void close() {
        if (null != parent) {
            parent.remove(this);
        }
    }

    public Border getTopBorder() {
        return topBorder;
    }

    public void setTopBorder(Border topBorder) {
        this.topBorder = topBorder;
    }

    public Border getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(Border bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public Border getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(Border leftBorder) {
        this.leftBorder = leftBorder;
    }

    public Border getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(Border rightBorder) {
        this.rightBorder = rightBorder;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Background getFocusedBackground() {
        return focusedBackground;
    }

    public void setFocusedBackground(Background focusedBackground) {
        this.focusedBackground = focusedBackground;
    }

    public List<Widget> getChildren() {
        return children;
    }

    public boolean isFocusable() {
        return true;
    }

    public boolean isFocusWhore() {
        return false;
    }

    public void onLeft() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onLeft();
        }
    }

    public void onRight() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onRight();
        }
    }

    public void onUp() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onUp();
        }
    }

    public void onDown() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onDown();
        }
    }

    public void onOK() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onOK();
        }
    }

    public void onCancel() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onCancel();
        }
    }

    public Widget getFocusedChild() {
        return null;
    }

    public abstract void accept(WidgetVisitor visitor);

    public void add(Widget child) {
        children.remove(child);
        children.add(child);
        child.setParent(this);
    }

    public void remove(Widget child) {
        if (null != child) {
            children.remove(child);
            child.setParent(null);
        }
    }

    public float getMinWidth() {
        return 0;
    }

    public float getMinHeight() {
        return 0;
    }

    public float getPreferredWidth() {
        return 0;
    }

    public float getPreferredHeight() {
        return 0;
    }

    public float getMaxWidth() {
        return 0;
    }

    public float getMaxHeight() {
        return 0;
    }

    public float getCenterX() {
        return x + width / 2.0f;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getCenterY() {
        return y + height / 2.0f;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Widget findClosestLeftFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterX() < widget.getCenterX()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    public Widget findClosestRightFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterX() > widget.getCenterX()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    public Widget findClosestUpFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterY() < widget.getCenterY()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    public Widget findClosestDownFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterY() > widget.getCenterY()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    private static float distanceSquared(Widget w1, Widget w2) {
        float dx = w1.getCenterX() - w2.getCenterX();
        float dy = w1.getCenterY() - w2.getCenterY();
        return dx * dx + dy * dy;
    }

    protected Widget getTopLeftFocusableChild() {
        final List<Widget> focusableChildren = getFocusableChildren();
        if (!focusableChildren.isEmpty()) {
            return Collections.min(focusableChildren, new Comparator<Widget>() {
                @Override
                public int compare(Widget w1, Widget w2) {
                    int result = Float.compare(w1.getCenterY(), w2.getCenterY());
                    if (result == 0) {
                        result = Float.compare(w1.getCenterX(), w2.getCenterX());
                    }
                    return result;
                }
            });
        } else {
            return null;
        }
    }

    private List<Widget> getFocusableChildren() {
        List<Widget> result = new ArrayList<Widget>();
        for (Widget w : getChildren()) {
            if (w.isFocusable()) {
                result.add(w);
            }
        }
        return result;
    }

    public void suckFocus() {
    }

    public boolean isSuckingFocus() {
        return false;
    }

    /**
     * update widget state
     *
     * @param delta time elapsed in seconds
     */
    public void update(float delta) {
        background.update(delta);
        focusedBackground.update(delta);
        leftBorder.update(delta);
        rightBorder.update(delta);
        topBorder.update(delta);
        bottomBorder.update(delta);
        for (Widget child : children) {
            child.update(delta);
        }
    }

    public void onMouseHover(float mouseX, float mouseY, boolean mouseButtonDown) {
        for (Widget child : children) {
            if (mouseX >= child.getX() && mouseX <= (child.getX() + child.getWidth()) && mouseY >= child.getY() && mouseY <= (child.getY() + child.getHeight())) {
                child.onMouseHover(mouseX, mouseY, mouseButtonDown);
            }
        }
    }

    public void onMouseMove(float mouseX, float mouseY) {
        for (Widget child : children) {
            if (mouseX >= child.getX() && mouseX <= (child.getX() + child.getWidth()) && mouseY >= child.getY() && mouseY <= (child.getY() + child.getHeight())) {
                child.onMouseMove(mouseX, mouseY);
            }
        }
    }

    public void onMouseClick(float mouseX, float mouseY) {
        for (Widget child : children) {
            if (mouseX >= child.getX() && mouseX <= (child.getX() + child.getWidth()) && mouseY >= child.getY() && mouseY <= (child.getY() + child.getHeight())) {
                child.onMouseClick(mouseX, mouseY);
            }
        }
    }

    public void onShow() {
    }

    public TextColor getFocusedTextColor() {
        return focusedTextColor;
    }

    public TextColor getSuckedFocusTextColor() {
        return suckedFocusTextColor;
    }

    public TextColor getTextColor() {
        return textColor;
    }

    public void setFocusedTextColor(TextColor focusedTextColor) {
        this.focusedTextColor = focusedTextColor;
    }

    public void setSuckedFocusTextColor(TextColor suckedFocusTextColor) {
        this.suckedFocusTextColor = suckedFocusTextColor;
    }

    public void setTextColor(TextColor textColor) {
        this.textColor = textColor;
    }

	public void onLoseFocus() {
	}

	public void onGainFocus() {
	}
}
