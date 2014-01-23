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
package im.bci.jnuit.lwjgl.assets;

import im.bci.tmxloader.TmxImage;
import im.bci.tmxloader.TmxLoader;
import im.bci.tmxloader.TmxMap;
import im.bci.tmxloader.TmxTile;
import im.bci.tmxloader.TmxTileset;

/**
 *
 * @author devnewton
 */
public class TmxAssetLoader {

    private final IAssets assets;

    public TmxAssetLoader(IAssets assets) {
        this.assets = assets;
    }
    
    public TmxAsset loadTmx(String mapFile) {
        final String mapParentDir = mapFile.substring(0, mapFile.lastIndexOf('/') + 1);
        TmxLoader loader = new TmxLoader();
        TmxMap map = new TmxMap();
        loader.parseTmx(map, assets.getText(mapFile));
        for (TmxTileset tileset : map.getTilesets()) {
            String tilesetParentDir;
            if (null != tileset.getSource()) {
                final String tilesetFile = mapParentDir + tileset.getSource();
                tilesetParentDir = tilesetFile.substring(0, tilesetFile.lastIndexOf('/') + 1);
                loader.parseTsx(map, tileset, assets.getText(tilesetFile));
            } else {
                tilesetParentDir = mapParentDir;
            }
            final TmxImage tilesetImage = tileset.getImage();
            if (null != tilesetImage) {
                tilesetImage.setSource(tilesetParentDir + tilesetImage.getSource());
            }
            for (TmxTile tile : tileset.getTiles()) {
                final TmxImage tileImage = tile.getFrame().getImage();
                if(tilesetImage != tileImage) {
                    tileImage.setSource(tilesetParentDir + tileImage.getSource());
                }
            }
        }
        loader.decode(map);
        return new TmxAsset(assets, map);
    }
}
