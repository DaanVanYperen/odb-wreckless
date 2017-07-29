package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.core.PassiveSystem;

/**
 * @author Daan van Yperen
 */
public class MapRenderSystem extends BaseSystem {

    private MapSystem mapSystem;
    private CameraSystem cameraSystem;

    public MyMapRendererImpl renderer;


    @Override
    protected void initialize() {
        renderer = new MyMapRendererImpl(mapSystem.map);
    }

    @Override
    protected void processSystem() {
        for (MapLayer layer : mapSystem.map.getLayers()) {
            if (layer.isVisible()) {
                if (!layer.getName().equals("infront")) {
                    renderLayer((TiledMapTileLayer) layer);
                }
            }
        }
    }

    private void renderLayer(final TiledMapTileLayer layer) {
        renderer.setView(cameraSystem.camera);
        renderer.renderLayer(layer);
    }
}