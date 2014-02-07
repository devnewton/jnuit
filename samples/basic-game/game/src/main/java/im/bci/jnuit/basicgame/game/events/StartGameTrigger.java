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
package im.bci.jnuit.basicgame.game.events;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import im.bci.jnuit.animation.PlayMode;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import com.google.inject.Inject;
import im.bci.jnuit.basicgame.game.Game;
import im.bci.jnuit.basicgame.game.Group;
import im.bci.jnuit.basicgame.game.NamedEntities;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.lwjgl.assets.TmxAsset;
import im.bci.jnuit.basicgame.game.components.Level;
import im.bci.jnuit.basicgame.game.components.ZOrder;
import im.bci.jnuit.basicgame.game.components.visual.Sprite;
import im.bci.jnuit.basicgame.game.components.visual.SpritePuppetControls;
import im.bci.jnuit.basicgame.game.constants.ZOrders;
import im.bci.jnuit.basicgame.game.systems.DrawSystem;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton natir
 */
public class StartGameTrigger extends Trigger {

    private final IAssets assets;
    private final Entity mainMenu;
    private final Entity ingameControls;
    private String levelName;
    private final Random random;

    @Inject
    public StartGameTrigger(IAssets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.IngameControls Entity ingameControls, Random random) {
        this.assets = assets;
        this.mainMenu = mainMenu;
        this.ingameControls = ingameControls;
        this.random = random;
    }

    public StartGameTrigger withLevelName(String levelName) {
        this.levelName = levelName;
        return this;
    }

    @Override
    public void process(Game game) {
        mainMenu.disable();
        ingameControls.enable();

        final GroupManager groupManager = game.getManager(GroupManager.class);

        ArrayList<Entity> entitiesToDelete = new ArrayList<>();
        for (Entity e : game.getManager(GroupManager.class).getEntities(Group.LEVEL)) {
            entitiesToDelete.add(e);
        }

        Entity level = game.createEntity();
        level.addComponent(new Level(assets.getAnimations("background.png").getFirst().start(PlayMode.ONCE)));
        level.addComponent(new ZOrder(ZOrders.LEVEL));
        groupManager.add(level, Group.LEVEL);
        game.addEntity(level);

        TmxAsset tmx = assets.getTmx(levelName);
        createProjector(game, tmx);
        final List<TmxLayer> layers = tmx.getLayers();
        for (int l = 0, n = layers.size(); l < n; ++l) {
            TmxLayer layer = tmx.getLayers().get(l);
            for (int y = 0, lh = layer.getHeight(); y < lh; ++y) {
                for (int x = 0, lw = layer.getWidth(); x < lw; ++x) {
                    final TmxTileInstance tile = layer.getTileAt(x, y);
                    if (null != tile) {
                        Entity entity = createEntityFromTile(tile, game, tmx, x, y, l, layer);
                        groupManager.add(entity, Group.LEVEL);
                    }
                }
            }
        }

        for (Entity e : entitiesToDelete) {
            e.deleteFromWorld();
        }
        assets.clearUseless();
    }

    private Vector3f tileToPos(TmxAsset tmx, int y, int x, int layer) {
        return new Vector3f(x, y, layer);
    }

    private Entity createEntityFromTile(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        switch (tile.getTile().getProperty("type", "decoration")) {
            default:
                return createDecoration(tile, game, tmx, x, y, l, layer);
        }

    }

    private Entity createDecoration(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity decoration = game.createEntity();
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_BELOW, decoration);
        game.addEntity(decoration);
        return decoration;
    }

    private enum ApparitionEffect {

        FROM_ABOVE,
        FROM_BELOW,
        NONE
    }

    private Sprite createSprite(TmxAsset tmx, int x, int y, int l, final TmxTileInstance tile, ApparitionEffect apparitionEffect, Entity entity) {
        Sprite sprite = new Sprite();
        final Vector3f pos = tileToPos(tmx, x, y, l);
        Vector3f apparitionPos = new Vector3f(pos);
        switch (apparitionEffect) {
            case FROM_ABOVE:
                apparitionPos.translate(0, 0, (1.0f + random.nextFloat()) * DrawSystem.SCREEN_HEIGHT);
                break;
            case FROM_BELOW:
                apparitionPos.translate((2.0f * random.nextFloat() - 1.0f) * tmx.getMap().getWidth(), (2.0f * random.nextFloat() - 1.0f) * tmx.getMap().getWidth(), (1.0f + random.nextFloat() * 2.0f) * -DrawSystem.SCREEN_HEIGHT);
                break;
            case NONE:
            default:
                break;
        }
        sprite.setPosition(apparitionPos);
        sprite.setWidth(tile.getTile().getFrame().getX2() - tile.getTile().getFrame().getX1());
        sprite.setHeight(tile.getTile().getFrame().getY2() - tile.getTile().getFrame().getY1());
        sprite.setPlay(tmx.getTileAnimationCollection(tile).getFirst().start(PlayMode.LOOP));
        final EnumSet<TmxTileInstanceEffect> effect = tile.getEffect();
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_HORIZONTALLY));
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_VERTICALLY));
        entity.addComponent(sprite);
        entity.addComponent(new ZOrder(ZOrders.LEVEL));
        entity.addComponent(new SpritePuppetControls(sprite).moveTo(pos, 2.0f));
        return sprite;
    }

    private void createProjector(Game game, TmxAsset tmx) {
        final float tileWidth = tmx.getMap().getTilewidth();
        final float tileHeight = tmx.getMap().getTileheight();
        game.getSystem(DrawSystem.class).setSpriteProjector(new IsometricSpriteProjector(tileWidth, tileHeight));
    }
}
