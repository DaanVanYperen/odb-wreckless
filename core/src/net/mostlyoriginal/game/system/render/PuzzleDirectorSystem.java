package net.mostlyoriginal.game.system.render;

import com.artemis.BaseSystem;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapSystem;

/**
 * Unfreeze near camera.
 *
 * @author Daan van Yperen
 */
public class PuzzleDirectorSystem extends BaseSystem {

    private CameraFollowSystem cameraFollowSystem;
    private EntitySpawnerSystem spawner;
    private MapSystem mapSystem;

    private int lastColumnPopulated = 0;

    private TiledMapTileLayer pitstopLayer;
    private TiledMapTile TILE_BLUE_PITSTOP;

    @Override
    protected void initialize() {
        super.initialize();

        TILE_BLUE_PITSTOP = mapSystem.map.getTileSets().getTile(111);
        pitstopLayer = (TiledMapTileLayer) mapSystem.map.getLayers().get("on top of behind");

        setTile(5, 5, TILE_BLUE_PITSTOP);

        TiledMapTile[] tiles = {TILE_BLUE_PITSTOP,TILE_BLUE_PITSTOP,TILE_BLUE_PITSTOP,TILE_BLUE_PITSTOP,TILE_BLUE_PITSTOP,TILE_BLUE_PITSTOP};

        spawnStrip( 5, 5, 5, 10, tiles);
    }

    private void spawnStrip(int x1, int y1, int x2, int y2, TiledMapTile[] tiles) {

        int index=0;
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                setTile(x, y, tiles[index++]);
            }

        }
    }

    private void setTile(int x, int y, TiledMapTile tile) {
        final TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tile);
        pitstopLayer.setCell(x, y, cell);
    }

    @Override
    protected void processSystem() {
//        int rightMostVisibleColumn = (int)(cameraFollowSystem.maxCameraX() / G.CELL_SIZE);
//
//        if (lastColumnPopulated <= rightMostVisibleColumn) {
//            populateColumns( rightMostVisibleColumn+1, 1, rightMostVisibleColumn + 8, 11);
//        }
    }
//
//    private void populateColumns(int x1, int y1, int x2, int y2) {
//
//        sprinkleCars(x1,y1,x2,y2, 6);
//        System.out.println("Populating "+x1+" to "+x2);
//
//        lastColumnPopulated = x2;
//    }
//
//    private void sprinkleCars(int x1, int y1, int x2, int y2, int count) {
//
//        for (int j = 0; j < count; j++) {
//            int x = MathUtils.random(x1,x2) * G.CELL_SIZE;
//            int y = MathUtils.random(y1,y2) * G.CELL_SIZE;
//            spawner.assembleCar(x,y, ChainColor.random().name());
//        }
//    }
}

