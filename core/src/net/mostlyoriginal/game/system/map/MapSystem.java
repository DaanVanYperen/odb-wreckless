package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class MapSystem extends BaseSystem {

    public TiledMap map;
    private int width;
    private int height;
    private Array<TiledMapTileLayer> layers;
    private boolean isSetup;

    private EntitySpawnerSystem entitySpawnerSystem;
    private GameScreenAssetSystem assetSystem;

    @Override
    protected void initialize() {
        map = new TmxMapLoader().load("map1.tmx");

        layers = new Array<TiledMapTileLayer>();
        for (MapLayer rawLayer : map.getLayers()) {
            layers.add((TiledMapTileLayer) rawLayer);
        }
        width = layers.get(0).getWidth();
        height = layers.get(0).getHeight();

        for (TiledMapTileSet tileSet : map.getTileSets()) {
            for (TiledMapTile tile : tileSet) {
                if (tile.getProperties().containsKey("entity")) {
                    Animation<TextureRegion> anim = new Animation<>(10, tile.getTextureRegion());
                    String id = (String) tile.getProperties().get("entity");
                    if (tile.getProperties().containsKey("cable-type")) {
                        id = tile.getProperties().get("entity")
                                + " "
                                + tile.getProperties().get("cable-type")
                                + "_"
                                + (((Boolean) tile.getProperties().get("cable-state")) ? "on" : "off");
                    } else if (tile.getProperties().containsKey("powered")) {
                        id = tile.getProperties().get("entity") + "_" + (((Boolean) tile.getProperties().get("powered")) ? "on" : "off");
                    }
                    assetSystem.sprites.put(id, anim);
                }
            }
        }
    }

    public MapMask getMask(String property) {
        return new MapMask(height, width, G.CELL_SIZE, G.CELL_SIZE, layers, property);
    }

    /**
     * Spawn map entities.
     */
    protected void setup() {
        for (TiledMapTileLayer layer : layers) {
            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if (cell != null) {
                        final MapProperties properties = cell.getTile().getProperties();
                        if (properties.containsKey("entity")) {
                            if (entitySpawnerSystem.spawnEntity(tx * G.CELL_SIZE, ty * G.CELL_SIZE, properties)) {
                                layer.setCell(tx, ty, null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void processSystem() {
        if (!isSetup) {
            isSetup = true;
            setup();
        }
    }

}