package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.api.component.physics.Inbetween;
import net.mostlyoriginal.game.component.G;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class EntitySpawnerSystem extends BaseSystem {
    @Override
    protected void processSystem() {
    }


    public void spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        spawnEntity(x, y, entity);
    }

    public void spawnEntity(float x, float y, String entity) {
        switch (entity) {
            case "player":
                assemblePlayer(x, y);
                break;
            default:
                //throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
    }

    private void assemblePlayer(float x, float y) {
        E().anim("player-idle")
                .pos(x,y)
                .physics()
                .render(G.LAYER_PLAYER)
                .gravity()
                .bounds(0,0, G.CELL_SIZE,G.CELL_SIZE)
                .wallSensor()
                .cameraFocus()
                .playerControlled();
    }
}
