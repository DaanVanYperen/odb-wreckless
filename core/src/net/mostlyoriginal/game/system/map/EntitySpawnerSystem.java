package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.Entity;
import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.api.component.physics.Inbetween;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.component.G;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class EntitySpawnerSystem extends BaseSystem {

    private SocketSystem socketSystem;
    private PowerSystem powerSystem;

    @Override
    protected void processSystem() {
    }


    public boolean spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        switch (entity) {
            case "player":
                assemblePlayer(x, y);
                break;
            case "exit":
                assembleExit(x, y);
                break;
            case "trigger":
                assembleTrigger(x, y, (String) properties.get("trigger"));
                break;
            case "robot":
                assembleRobot(x, y);
                break;
            case "battery":
                assembleBattery(x, y);
                break;
            case "socket":
                assembleBatterySlot(x, y, (Boolean) properties.get("powered"));
                break;
            default:
                return false;
            //throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
        return true;
    }

    private void assembleTrigger(float x, float y, String trigger) {
        E().pos(x,y - 5000).bounds(0,0,16,10000).trigger(trigger);
    }

    private void assembleBatterySlot(float x, float y, boolean b) {
        E socket = E();
        socket.anim()
                .pos(x, y)
                .socketAnimSocketed("socket_on")
                .socketAnimEmpty("socket_off")
                .render(G.LAYER_PLAYER - 1)
                .bounds(0, 0, G.CELL_SIZE, G.CELL_SIZE);

        if (b) {
            socketSystem.socket(assembleBattery(x, y), socket);
            powerSystem.powerMapCoordsAround((int) (x / G.CELL_SIZE + 0.5f), (int) (y / G.CELL_SIZE + 0.5f), true);
        } else {
            powerSystem.powerMapCoordsAround((int) (x / G.CELL_SIZE + 0.5f), (int) (y / G.CELL_SIZE + 0.5f), false);
        }


    }

    private void assemblePlayer(float x, float y) {
        E().anim("player-idle")
                .pos(x, y)
                .physics()
                .render(G.LAYER_PLAYER)
                .gravity()
                .bounds(0, 0, 16, 17)
                .wallSensor()
                .cameraFocus()
                .tag("player")
                .playerControlled();
    }

    private void assembleExit(float x, float y) {
        E().anim("exit")
                .pos(x, y)
                .render(G.LAYER_PLAYER - 100)
                .exit()
                .bounds(0, 0, 16, 16);
    }

    private E assembleBattery(float x, float y) {
        return E().anim("battery")
                .pos(x, y)
                .physics().pickup()

                .render(G.LAYER_PLAYER - 1)
                .gravity()
                .bounds(0, 0, G.CELL_SIZE, G.CELL_SIZE)
                .wallSensor();
    }

    private void assembleRobot(float x, float y) {
        E().anim("robot-idle")
                .pos(x, y)
                .physics()
                .render(G.LAYER_PLAYER_ROBOT)
                .follow()
                .gravity()
                .platform()
                .bounds(0, 0, 39, 43)
                .tag("robot")
                .wallSensor();
    }
}
