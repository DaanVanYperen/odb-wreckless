package net.mostlyoriginal.game.system.map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.api.utils.MapMask;

import java.util.HashMap;

/**
 * @author Daan van Yperen
 */
public class PowerSystem extends PassiveSystem {

    private MapMask cableMask;
    private MapMask cableMask2;
    private MapSystem mapSystem;

    private HashMap<Integer, TiledMapTile> tilesOn = new HashMap<>();
    private HashMap<Integer, TiledMapTile> tilesOff = new HashMap<>();

    @Override
    protected void initialize() {
        super.initialize();
        cableMask = mapSystem.getMask("cable-type");

        for (TiledMapTileSet tileSet : mapSystem.map.getTileSets()) {
            for (TiledMapTile tile : tileSet) {
                MapProperties properties = tile.getProperties();
                if (properties.containsKey("cable-type")) {
                    if ((Boolean) properties.get("cable-state")) {
                        tilesOn.put((Integer) properties.get("cable-type"), tile);
                    } else {
                        tilesOff.put((Integer) properties.get("cable-type"), tile);
                    }
                }
            }
        }
    }

    public void powerMapCoords(int x, int y, boolean enable) {
        if (cableMask.atGrid(x, y, false)) {
            TiledMapTileLayer cableLayer = (TiledMapTileLayer) mapSystem.map.getLayers().get("cables");
            final TiledMapTileLayer.Cell cell = cableLayer.getCell(x, y);
            if (cell != null) {
                MapProperties properties = cell.getTile().getProperties();
                if (properties.containsKey("cable-state")) {
                    if ((Boolean) properties.get("cable-state") != enable) {
                        cell.setTile(enable ?
                                tilesOn.get((Integer) properties.get("cable-type")) :
                                tilesOff.get((Integer) properties.get("cable-type")));
                        powerMapCoordsAround(x, y, enable);
                    }
                }
            }
        }
    }

    public void powerMapCoordsAround(int x, int y, boolean enable) {
        powerMapCoords(x - 1, y, enable);
        powerMapCoords(x + 1, y, enable);
        powerMapCoords(x, y + 1, enable);
        powerMapCoords(x, y - 1, enable);
    }
}

