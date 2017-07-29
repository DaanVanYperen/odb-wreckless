package net.mostlyoriginal.game.system.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * @author Daan van Yperen
 */
public class MyMapRendererImpl extends OrthogonalTiledMapRenderer {
    public MyMapRendererImpl(TiledMap map) {
        super(map);
    }

    public void renderLayer(TiledMapTileLayer layer) {
        getBatch().setColor(1f, 1f, 1f, 1f);
        beginRender();
        super.renderTileLayer(layer);
        endRender();
    }
}