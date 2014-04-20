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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Graphics;
import playn.core.PlayN;
import playn.core.TextLayout;

/**
 *
 * @author devnewton
 */
public class PlaynTextCache {

    private final PlaynNuitFont font;
    private final HashMap<CachedTextKey, CachedTextImage> cache = new HashMap<CachedTextKey, CachedTextImage>();

    private static final double LIFETIME = 1000;

    private static class CachedTextKey {

        String text;
        int color;

        public CachedTextKey(String text, int color) {
            this.text = text;
            this.color = color;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + (this.text != null ? this.text.hashCode() : 0);
            hash = 29 * hash + this.color;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CachedTextKey other = (CachedTextKey) obj;
            if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
                return false;
            }
            return this.color == other.color;
        }

    }

    private static class CachedTextImage {

        CanvasImage image;
        double timeToDie;
    }

    public PlaynTextCache(PlaynNuitFont font) {
        this.font = font;
    }

    public void clearUseless() {
        Iterator<Map.Entry<CachedTextKey, CachedTextImage>> it = cache.entrySet().iterator();
        final double currentTime = PlayN.currentTime();
        while (it.hasNext()) {
            Map.Entry<CachedTextKey, CachedTextImage> entry = it.next();
            final CachedTextImage cached = entry.getValue();
            if (currentTime > cached.timeToDie) {
                it.remove();
            }
        }
    }

    public CanvasImage getTextCanvasImage(String text, int color) {
        CachedTextKey key = new CachedTextKey(text, color);
        CachedTextImage cached = cache.get(key);
        if (null == cached) {
            cached = new CachedTextImage();
            cached.image = createTextCanvasImage(text, color);
            cache.put(key, cached);
        }
        cached.timeToDie = PlayN.currentTime() + LIFETIME;
        return cached.image;
    }

    private CanvasImage createTextCanvasImage(String text, int color) {
        final Graphics graphics = PlayN.graphics();
        TextLayout textLayout = graphics.layoutText(text, font.format);
        CanvasImage textImage = graphics.createImage(textLayout.width(), textLayout.height());
        final Canvas canvas = textImage.canvas();
        canvas.setFillColor(Color.argb(0, 123, 123, 123));
        canvas.fillRect(0, 0, canvas.width(), canvas.height());
        canvas.setFillColor(color);
        canvas.fillText(textLayout, 0, 0);
        return textImage;
    }
}
