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
package im.bci.jnuit.lwjgl.sprite;

import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IAnimationImage;
import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpriteProjector;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import pythagoras.f.Vector;

/**
 *
 * @author devnewton
 */
public class SpriteBatcher {

    private static final int BATCH_SIZE = 1024;
    private final FloatBuffer vertices = ByteBuffer.allocateDirect(BATCH_SIZE * 4 * 2 * Float.SIZE / 8).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private final FloatBuffer texCoords = ByteBuffer.allocateDirect(BATCH_SIZE * 4 * 2 * Float.SIZE / 8).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private IAnimationImage currentImage = null;
    private final Color currentColor = new Color();
    private int currentSpriteInBatch = 0;
    private SpriteProjector spriteProjector;

    public void draw(Sprite sprite) {
        IPlay play = sprite.getPlay();
        if (null != play) {

            final IAnimationFrame frame = play.getCurrentFrame();
            final IAnimationImage image = frame.getImage();
            final Color color = new Color((int)(sprite.getRed() * 255), (int)(sprite.getGreen() * 255), (int)(sprite.getBlue() * 255), (int)(sprite.getAlpha() * 255));
            flushIfNeeded(image, color);

            Vector pos = spriteProjector.project(sprite.getPosition());
            float scale = sprite.getScale() / 2.0f;
            float w = sprite.getWidth() * scale;
            float h = sprite.getHeight() * scale;
            final float u1, u2;
            if (sprite.isMirrorX()) {
                u1 = frame.getU2();
                u2 = frame.getU1();
            } else {
                u1 = frame.getU1();
                u2 = frame.getU2();
            }

            final float v1, v2;
            if (sprite.isMirrorY()) {
                v1 = frame.getV1();
                v2 = frame.getV2();
            } else {
                v1 = frame.getV2();
                v2 = frame.getV1();
            }

            final float rotate = sprite.getRotate();
            if (rotate == 0.0f) {
                float x1 = pos.x - w;
                float x2 = pos.x + w;
                float y1 = pos.y - h;
                float y2 = pos.y + h;
                vertex(x1, y2, u1, v1);
                vertex(x2, y2, u2, v1);
                vertex(x2, y1, u2, v2);
                vertex(x1, y1, u1, v2);
            } else {
                float cosr = (float) Math.cos(rotate);
                float sinr = (float) Math.sin(rotate);
                float x1 = pos.x + -w * cosr - h * sinr;
                float y1 = pos.y + h * cosr + -w * sinr;
                float x2 = pos.x + w * cosr - h * sinr;
                float y2 = pos.y + h * cosr + w * sinr;
                float x3 = pos.x + w * cosr - -h * sinr;
                float y3 = pos.y + -h * cosr + w * sinr;
                float x4 = pos.x + -w * cosr - -h * sinr;
                float y4 = pos.y + -h * cosr + -w * sinr;
                
                vertex(x1, y1, u1, v1);
                vertex(x2, y2, u2, v1);
                vertex(x3, y3, u2, v2);
                vertex(x4, y4, u1, v2);
                /*float x1 = -w;
                float y1 = h;
                float x2 = w;
                float y2 = h;
                float x3 = w;
                float y3 = -h;
                float x4 = -w;
                float y4 = -h;*/
            }

            //x' = x cos f - y sin f
            //y' = y cos f + x sin f
            ++currentSpriteInBatch;
        }
    }

    private void vertex(float x, float y, float u, float v) {
        vertices.put(x);
        vertices.put(y);
        texCoords.put(u);
        texCoords.put(v);
    }

    private void flushIfNeeded(IAnimationImage image, Color color) {
        if (currentImage != image || !currentColor.equals(color) || currentSpriteInBatch == BATCH_SIZE) {
            flush();
        }
        currentColor.setColor(color);
        currentImage = image;
    }

    private void flush() {
        if (currentSpriteInBatch > 0) {
            boolean hasAlpha = true;
            if (hasAlpha) {
                GL11.glEnable(GL11.GL_BLEND);
            }
            texCoords.flip();
            vertices.flip();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, (Integer)currentImage.getId());
            GL11.glTexCoordPointer(2, 0, texCoords);
            GL11.glVertexPointer(2, 0, vertices);
            GL11.glColor4ub(currentColor.getRedByte(), currentColor.getGreenByte(), currentColor.getBlueByte(), currentColor.getAlphaByte());
            GL11.glDrawArrays(GL11.GL_QUADS, 0, currentSpriteInBatch * 4);
            if (hasAlpha) {
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glColor3f(1f, 1f, 1f);
            texCoords.rewind();
            texCoords.limit(texCoords.capacity());
            vertices.rewind();
            vertices.limit(texCoords.capacity());

            currentSpriteInBatch = 0;
        }
    }

    public void begin() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void end() {
        flush();
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public void setSpriteProjector(SpriteProjector spriteProjector) {
        this.spriteProjector = spriteProjector;
    }
}
