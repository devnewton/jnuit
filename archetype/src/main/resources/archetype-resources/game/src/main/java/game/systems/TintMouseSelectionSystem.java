#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${game-package}.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import ${game-package}.game.components.ZOrder;
import im.bci.jnuit.artemis.sprite.Sprite;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Color;
import pythagoras.f.Vector3;
/**
 *
 * @author devnewton
 */
public class TintMouseSelectionSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Sprite> spriteMapper;
    private Vector3 mousePos;
    private float nearestSpriteDistance;
    private Sprite nearestSprite;
    private Entity debugMouseSprite;

    public TintMouseSelectionSystem() {
        super(Aspect.getAspectForAll(Sprite.class));
    }

    @Override
    protected void begin() {
        if (null != nearestSprite) {
            nearestSprite.setRed(1f);
            nearestSprite.setGreen(1f);
            nearestSprite.setBlue(1f);
            nearestSprite.setAlpha(1f);
            nearestSprite = null;
        }
        nearestSpriteDistance = Float.MAX_VALUE;
        mousePos = world.getSystem(DrawSystem.class).getMouseSpritePos(10);
        if (null != mousePos) {
            if (null == debugMouseSprite) {
                debugMouseSprite = world.createEntity();
                debugMouseSprite.addComponent(new Sprite());
                debugMouseSprite.addComponent(new ZOrder(100));
                world.addEntity(debugMouseSprite);
            }
            spriteMapper.get(debugMouseSprite).setPosition(new Vector3(mousePos));
            spriteMapper.get(debugMouseSprite).setLabel(Mouse.getX() + "," + Mouse.getY());
        }
    }

    @Override
    protected void process(Entity entity) {
        if (null != mousePos && entity != debugMouseSprite) {
            Sprite sprite = spriteMapper.get(entity);
            Vector3 v = mousePos.subtract(sprite.getPosition());
            float distance = v.lengthSquared();
            if (distance < nearestSpriteDistance) {
                nearestSprite = sprite;
                nearestSpriteDistance = distance;
            }
        }
    }

    @Override
    protected void end() {
        if (null != nearestSprite) {
            nearestSprite.setRed(1f);
            nearestSprite.setGreen(0.5f);
            nearestSprite.setBlue(0f);
            nearestSprite.setAlpha(1f);
        }
    }

    Sprite getSelectedSprite() {
        return nearestSprite;
    }
}
