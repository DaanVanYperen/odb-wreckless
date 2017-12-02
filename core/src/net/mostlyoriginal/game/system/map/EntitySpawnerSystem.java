package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Spout;
import net.mostlyoriginal.game.system.detection.SpoutSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;
import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class EntitySpawnerSystem extends BaseSystem {

    private SocketSystem socketSystem;
    private PowerSystem powerSystem;
    private SpoutSystem spoutSystem;
    private GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void processSystem() {
    }


    public boolean spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        switch (entity) {
            case "player":
                assemblePlayer(x, y);
                break;
            case "enemy1":
                assembleEnemy(x, y, 1);
                break;
            case "enemy2":
                assembleEnemy(x, y, 2);
                break;
            case "enemy3":
                assembleEnemy(x, y, 3);
                break;
            case "enemy4":
                assembleEnemy(x, y, 4);
                break;
            case "enemy5":
                assembleEnemy(x, y, 5);
                break;
            case "exit":
                assembleExit(x, y);
                break;
            case "trigger":
                assembleTrigger(x, y, (String) properties.get("trigger"), (String) properties.get("parameter"));
                break;
            case "robot":
                E robot = assembleRobot(x, y);
                robot.chargeCharge(3).flying(true);
                break;
//            case "battery":
//                assembleBattery(x, y, "battery");
//                break;
//            case "battery2":
//                assembleBattery(x, y, "battery2");
//                break;
//            case "gremlin":
//                spoutSystem.spawnGremlin(0,x,y);
//                return true;
            case "birds":
                for (int i = 0, s = MathUtils.random(1, 3); i <= s; i++) {
                    assembleBird(x + MathUtils.random(G.CELL_SIZE), y + MathUtils.random(G.CELL_SIZE));
                }
                return true;
            case "spout":
                assembleSpout(x, y, (Integer) properties.get("angle"), "ACID");
                return false;
            case "spawner":
                assembleSpout(x, y, (Integer) properties.get("angle"), (String) properties.get("spawns"))
                        .spoutSprayInterval(0.5f).spoutSprayDuration(1);
                return false;
            case "socket":
                assembleBatterySlot(x, y, (Boolean) properties.get("powered"), (String) properties.get("accept"));
                break;
            case "sandsprinkler":
                assembleSandSprinkler(x, y - 1);
                return false;
            default:
                return false;
            //throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
        return true;
    }

    private void assembleSandSprinkler(float x, float y) {
        E.E().pos(x, y).bounds(0, 0, 16, 1).sandSprinkler();
    }

    private int birdIndex = 0;

    private void assembleBird(float x, float y) {
        String birdType = "bird-" + MathUtils.random(1, 3);
        E().pos(x, y)
                .bounds(0, 0, 2, 2)
                .anim()
                .renderLayer(G.LAYER_BIRDS + birdIndex++)
                .animFlippedX(MathUtils.randomBoolean())
                .birdBrain()
                .birdBrainAnimIdle(birdType + "-idle")
                .birdBrainAnimFlying(birdType + "-flying")
                .gravityY(-0.2f)
                .physics()
                .teamTeam(2)
                .wallSensor();

    }

    private E assembleSpout(float x, float y, Integer angle, String spawns) {
        return E().pos(x, y).bounds(0, 0, 16, 16).spoutAngle(angle).spoutType(Spout.Type.valueOf(spawns));
    }

    private void assembleTrigger(float x, float y, String trigger, String parameter) {
        boolean tallTrigger = !trigger.equals("music");
        E().pos(x, y - (tallTrigger ? 5000 : 0)).bounds(0, 0, 16, (tallTrigger ? 10000 : 16)).trigger(trigger).triggerParameter(parameter);
    }

    private void assembleBatterySlot(float x, float y, boolean b, String batteryType) {
        E socket = E();
        socket.anim()
                .pos(x, y)
                .socketAnimSocketed("socket_on_" + batteryType)
                .socketAnimEmpty("socket_off_" + batteryType)
                .type(batteryType)
                .render(G.LAYER_PLAYER - 1)
                .bounds(0, 0, G.CELL_SIZE, G.CELL_SIZE);

        if (b) {
            spawnBatteryInSocket(batteryType, socket);
        } else {
            powerSystem.powerMapCoordsAround((int) (x / G.CELL_SIZE + 0.5f), (int) (y / G.CELL_SIZE + 0.5f), false);
        }


    }

    public void spawnBatteryInSocket(String batteryType, E socket) {
        socketSystem.socket(assembleBattery(socket.posX(), socket.posY(), batteryType), socket);
        powerSystem.powerMapCoordsAround((int) (socket.posX() / G.CELL_SIZE + 0.5f), (int) (socket.posY() / G.CELL_SIZE + 0.5f), true);
    }

    Vector2 v2 = new Vector2();

    private void assemblePlayer(float x, float y) {
        int gracepaddingX=16;
        int gracepaddingY=4;
        int entity = E().anim("player-idle")
                .pos(x, y)
                .physics()
                .render(G.LAYER_PLAYER)
                .mortal()
                //.gravity()
                .wallSensor()
                .cameraFocus()
                .teamTeam(G.TEAM_PLAYERS)
                .footsteps()
                .footstepsSfx("footsteps_girl")
                .tag("player")
                .shipControlled()
                .id();

        gameScreenAssetSystem.boundToAnim(entity, gracepaddingX, gracepaddingY);

        float directions = 16f;
        float angle = 0;
        for (int i = 0; i < directions; i++) {
            angle += 360 / directions * i;
            v2.set(20, 0).rotate(angle);
            E().anim("marker")
                    .render(G.LAYER_PLAYER + 1)
                    .pos(x, y)
                    .bounds(0, 0, 5, 5)
                    .group("player-guns")
                    .attachedXo((int) v2.x + PLAYER_WIDTH / 2 - 3)
                    .attachedYo((int) v2.y + PLAYER_HEIGHT / 2 - 3)
                    .attachedParent(entity)
                    .teamTeam(G.TEAM_PLAYERS)
                    .spoutType(Spout.Type.BULLET)
                    .angleRotate(angle);
        }
    }

    private void assembleExit(float x, float y) {
        E().anim("exit")
                .pos(x, y)
                .render(G.LAYER_PLAYER - 100)
                .exit()
                .bounds(0, 0, 16, 16);
    }

    private E assembleBattery(float x, float y, String batteryType) {
        return E().anim(batteryType)
                .pos(x, y)
                .physics().pickup()
                .type(batteryType)
                .render(G.LAYER_PLAYER - 1)
                .gravity()
                .bounds(-8, -8, 24, 24)
                .wallSensor();
    }

    private E assembleRobot(float x, float y) {
        E robot = E().anim("robot-idle")
                .pos(x, y)
                .physics()
                .charge()
                .socketAnimSocketed(null)
                .socketAnimEmpty(null)
                .socketSfxSocketed("battery_eaten")
                .type("battery2")
                .running()
                .robot()
                .teamTeam(G.TEAM_PLAYERS)
                .render(G.LAYER_PLAYER_ROBOT)
                .follow()
                .footstepsStepSize(8)
                .footstepsSfx("footsteps_robot")
                .gravity()
                .platform()
                .bounds(0, 0, 39, 43)
                .tag("robot")
                .wallSensor();

        E().anim("robot-idle")
                .tag("robot-charge").pos(x, y).bounds(0, 0, 25, 12);

        return robot;
    }

    private void assembleEnemy(float x, float y, int type) {
        int gracepaddingX=8;
        int gracepaddingY=0;
        int enemyId = E()
                .pos(x, y)
                .physics()
                .physicsVy(-10f)
                .physicsFriction(0)
                .mortal()
                .deadly()
                .teamTeam(TEAM_ENEMIES)
                .render(G.LAYER_GREMLIN)
                .anim("enemy-" + type).id();


        gameScreenAssetSystem.boundToAnim(enemyId, gracepaddingX, gracepaddingY);

    }


    public void spawnGremlin(float x, float y) {
        E robot = E().anim("gremlin-1-idle")
                .pos(x, y)
                .physics()
                .mortal()
                .jumpAttack()
                .deadly()
                .render(G.LAYER_GREMLIN)
                .footstepsStepSize(4)
                .gravity()
                .bounds(0, 0, 24, 24)
                .wallSensor();
    }
}
