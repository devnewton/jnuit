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

package im.bci.jnuit.basic.game.events;

import im.bci.jnuit.basic.game.systems.SpriteProjector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class OrthogonalSpriteProjector implements SpriteProjector {
    private final float tileWidth;
    private final float tileHeight;

    public OrthogonalSpriteProjector(float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    public Vector2f project(Vector3f pos) {
        return new Vector2f(pos.y * tileWidth, pos.x * tileHeight);
    }
    @Override
    public Vector3f unProject(Vector2f screenPos) {
        float tx = screenPos.x / tileWidth;
        float ty = screenPos.y / tileHeight;
        return new Vector3f(tx, ty, 0.0f);
    }
    @Override
    public int compare(Vector3f v1, Vector3f v2) {
        return Float.compare(v1.z, v2.z);
    }
    
}
