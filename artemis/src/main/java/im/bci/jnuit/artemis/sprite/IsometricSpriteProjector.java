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

package im.bci.jnuit.artemis.sprite;

import pythagoras.f.Vector;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
public class IsometricSpriteProjector implements SpriteProjector {
    private final float tileWidth;
    private final float tileHeight;

    public IsometricSpriteProjector(float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    public Vector project(Vector3 pos) {
        return new Vector(-(pos.x - pos.y) * tileWidth / 2.0f, (pos.x + pos.y) * tileHeight / 2.0f - pos.z);
    }
    @Override
    public Vector3 unProject(Vector screenPos) {
        float tx = screenPos.x / tileWidth;
        float ty = screenPos.y / tileHeight;
        return new Vector3(ty - tx, tx + ty, 0.0f);
    }
    @Override
    public int compare(Vector3 v1, Vector3 v2) {
        float d1 = v1.x + v1.y + v1.z;
        float d2 = v2.x + v2.y + v2.z;
        if (Math.abs(d1 - d2) > 0.1f) {
            return Float.compare(d1, d2);
        } else {
            return 0;
        }
    }
    
}
