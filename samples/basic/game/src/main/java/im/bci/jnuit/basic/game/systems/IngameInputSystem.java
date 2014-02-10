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
package im.bci.jnuit.basic.game.systems;

import im.bci.jnuit.basic.game.events.ShowMenuTrigger;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import im.bci.jnuit.controls.Action;
import im.bci.jnuit.controls.ActionActivatedDetector;
import im.bci.jnuit.lwjgl.controls.MouseButtonControl;

import im.bci.jnuit.basic.game.Game;
import im.bci.jnuit.basic.game.components.Triggerable;
import im.bci.jnuit.basic.game.components.IngameControls;
import im.bci.jnuit.basic.game.components.visual.Sprite;
import im.bci.jnuit.basic.game.components.visual.SpritePuppetControls;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
@Singleton
public class IngameInputSystem extends EntityProcessingSystem {

    private final Provider<ShowMenuTrigger> showMenuTrigger;
    private final ActionActivatedDetector mouseClick = new ActionActivatedDetector(new Action("click", new MouseButtonControl(0)));

    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    @Inject
    public IngameInputSystem(Provider<ShowMenuTrigger> showMenuTrigger) {
        super(Aspect.getAspectForAll(IngameControls.class));
        this.showMenuTrigger = showMenuTrigger;
    }

    @Override
    protected void process(Entity e) {
        if (e.isEnabled()) {
            Game game = (Game) world;

            IngameControls controls = e.getComponent(IngameControls.class);
            controls.getShowMenu().poll();
            if (controls.getShowMenu().isActivated()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(showMenuTrigger.get())));
            }
            if (canMoveHero()) {
                controls.getUp().poll();
                controls.getDown().poll();
                controls.getRight().poll();
                controls.getLeft().poll();
                mouseClick.poll();
                boolean upPressed = controls.getUp().isPressed();
                boolean downPressed = controls.getDown().isPressed();
                boolean leftPressed = controls.getLeft().isPressed();
                boolean rightPressed = controls.getRight().isPressed();
                Entity hero = game.getHero();
                if (mouseClick.isPressed()) {
                    Vector3f selectedPosition = game.getSystem(TintMouseSelectionSystem.class).getSelectedSprite().getPosition();
                    Vector3f heroPosition = spriteMapper.get(hero).getPosition();
                    int heroX = Math.round(heroPosition.x);
                    int heroY = Math.round(heroPosition.y);
                    int selectedX = Math.round(selectedPosition.x);
                    int selectedY = Math.round(selectedPosition.y);

                    if (heroX == selectedX) {
                        if (heroY < selectedY) {
                            rightPressed = true;
                        } else if (heroY > selectedY) {
                            leftPressed = true;
                        }
                    } else if (heroY == selectedY) {
                        if (heroX < selectedX) {
                            downPressed = true;
                        } else if (heroX > selectedX) {
                            upPressed = true;
                        }
                    }
                }
                if (upPressed) {
                    hero.addComponent(new SpritePuppetControls(spriteMapper.get(hero)).moveToRelative(new Vector3f(-1f,0f, 0f), 0.5f));
                    hero.changedInWorld();
                } else if (downPressed) {
                    hero.addComponent(new SpritePuppetControls(spriteMapper.get(hero)).moveToRelative(new Vector3f(1f,0f, 0f), 0.5f));
                    hero.changedInWorld();
                } else if (leftPressed) {
                    hero.addComponent(new SpritePuppetControls(spriteMapper.get(hero)).moveToRelative(new Vector3f(0f, -1f, 0f), 0.5f));
                    hero.changedInWorld();
                } else if (rightPressed) {
                    hero.addComponent(new SpritePuppetControls(spriteMapper.get(hero)).moveToRelative(new Vector3f(0f, 1f, 0f), 0.5f));
                    hero.changedInWorld();
                }
            }
        }
    }

    private boolean canMoveHero() {
        return world.getSystem(SpritePuppetControlSystem.class).getActives().isEmpty();
    }
}
