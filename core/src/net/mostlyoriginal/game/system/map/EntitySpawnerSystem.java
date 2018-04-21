package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.TowedSystem;
import net.mostlyoriginal.game.system.detection.PickupSystem;
import net.mostlyoriginal.game.system.view.*;

import static com.artemis.E.E;
import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class EntitySpawnerSystem extends BaseSystem {

    //    private SocketSystem socketSystem;
//    private PowerSystem powerSystem;
//    private SpoutSystem spoutSystem;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private ShipDataSystem shipDataSystem;
    private ArsenalDataSystem arsenalDataSystem;
    private FlightPatternDataSystem flightPatternDataSystem;
    private PickupSystem pickupSystem;
    private TowedSystem towedSystem;

    @Override
    protected void processSystem() {
    }


    public boolean spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        switch (entity) {
            case "player":
                assemblePlayer(x, y, shipDataSystem.get("player"));
                break;
            case "car":
                assembleCar((int) x, (int) y, (String) properties.get("color"));
                break;
            case "pitstop":
                assemblePitstop((int) x, (int) y, (String) properties.get("color"));
                return false;
            case "birds":
                for (int i = 0, s = MathUtils.random(1, 3); i <= s; i++) {
                    assembleBird(x + MathUtils.random(G.CELL_SIZE), y + MathUtils.random(G.CELL_SIZE));
                }
                return true;
            case "powerup":
                assemblePowerup(x, y);
                return true;
            default:
                ShipData shipData = shipDataSystem.get(entity);
                if (shipData != null) {
                    assembleEnemy(x, y, shipData);
                    return true;
                }
                return false;
            //throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
        return true;
    }

    private E assembleCar(int x, int y, String color) {
        final E e = E()
                .pos(x, y)
                .angle()
                .render(G.LAYER_GREMLIN)
                .crashable()
                .snapToGrid()
                .towable()
                .frozen(true)
                .teamTeam(TEAM_ENEMIES)
                .chainableColor(ChainColor.valueOf(color))
                .snapToGridX(x / G.CELL_SIZE)
                .snapToGridY(y / G.CELL_SIZE)
                .anim("car-" + color);
        gameScreenAssetSystem.boundToAnim(e.id(), 0, 0);
        return e;


    }

    private E assemblePitstop(int x, int y, String color) {
        final E e = E()
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .teamTeam(TEAM_ENEMIES)
                .chainableColor(ChainColor.valueOf(color))
                .chainablePitstop(true).bounds(0,0,G.CELL_SIZE,G.CELL_SIZE);
        return e;
    }

    private void assemblePowerup(float x, float y) {
        E().pos(x + 8 - 14, y + 8 - 16)
                .bounds(0, 0, 28, 16)
                .anim("pickup")
                .pickup()
                .frozen()
                .physicsFriction(0)
                .angle()
                .ethereal()
                .physicsVr(-100f)
                .physicsVy(-75f)
                .renderLayer(990);
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

    Vector2 v2 = new Vector2();

    private void assemblePlayer(float x, float y, ShipData shipData) {
        int gracepaddingX = 16;
        int gracepaddingY = 4;
        E playerCar = E().anim("player-idle")
                .pos(x - 14, y)
                .render(G.LAYER_PLAYER)
                .snapToGridX((int) x / G.CELL_SIZE)
                .snapToGridY((int) y / G.CELL_SIZE)
                .snapToGridPixelsPerSecondX(196)
                .mortal()
                //.gravity()
                .wallSensor()
                .player()
                .diesFromWalls(true)
                .teamTeam(G.TEAM_PLAYERS)
                .tag("player")
                .shipControlled()
                .shieldHp(shipData.hp)
                .shieldMaxHp(shipData.hp);

//        E().anim("player-hook")
//                .pos(x, y)
//                .attachedXo(-28)
//                .attachedYo(-12)
//                .attachedParent(playerCar.id())
//                .renderLayer(G.LAYER_GREMLIN - 1);
//
//        E().anim("player-idle")
//                .pos(0, 0)
//                .render(G.LAYER_PLAYER)
//                .tint(0f, 0f, 1f, 0.5f)
//                .tag("control-ghost");
//
//        E().anim("thruster")
//                .pos(x, y)
//                .attachedXo(14)
//                .attachedYo(-18)
//                .attachedParent(playerCar.id())
//                .renderLayer(G.LAYER_PLAYER - 1);
//
//        E().anim("thruster")
//                .pos(x, y)
//                .attachedXo(26)
//                .attachedYo(-18)
//                .attachedParent(playerCar.id())
//                .renderLayer(G.LAYER_PLAYER - 1);


        gameScreenAssetSystem.boundToAnim(playerCar.id(), gracepaddingX, gracepaddingY);

//        pickupSystem.upgradeGuns(playerCar);

//        final E c1 = assembleCar((int) x, (int) y, "RED");
//        final E c2 = assembleCar((int) x, (int) y, "GREEN");
//        final E c3 = assembleCar((int) x, (int) y, "BLUE");
//        towedSystem.hookOnto(playerCar, c1);
//        towedSystem.hookOnto(c1, c2);
//        towedSystem.hookOnto(c2, c3);

        //towedSystem.hookTo(playerCar,assembleCar(x,y, "RED"));
        spawnCamera(x, y);
    }

    private void spawnCamera(float x, float y) {
        E()
                .pos(x, y)
                .cameraFocus()
                .physicsVx(50f)
                .tag("camera")
                .ethereal(true)
                .physicsFriction(0);

    }

    public void addArsenal(E ship, String group, int team, int shipFacingAngle, String arsenal, boolean frozen) {
        if (arsenal != null) {
            ArsenalData data = arsenalDataSystem.get(arsenal);
            if (data.guns != null) {
                for (GunData gun : data.guns) {
                    addGun(ship, gun, group, team, shipFacingAngle, frozen);
                }
            }
        }
    }

    private void addGun(E e, GunData gunData, String group, int team, int shipFacingAngle, boolean frozen) {
        float angle = gunData.angle + shipFacingAngle + 90;
        v2.set(0, gunData.x).rotate(angle);
        E gun = E()
                .pos(e.posX(), e.posY())
                .bounds(0, 0, 5, 5)
                .group(group)
                .attachedXo((int) v2.x + (int) e.boundsCx() - 3)
                .attachedYo((int) v2.y + (int) e.boundsCy() - 3)
                .attachedParent(e.id())
                .teamTeam(team)
                .spoutAngle(0)
                .spoutType(Spout.Type.BULLET)
                .spoutSprayInterval(60f / gunData.rpm)
                .gunData(gunData)
                .angleRotate(angle);

        if (gunData.cooldown > 0) {
            gun.spoutCooldown(gunData.cooldown);
        }
        if (gunData.duration > 0) {
            gun.spoutSprayDuration(gunData.duration);
        }

        if (frozen) {
            gun.frozen();
        }

        if (team == TEAM_ENEMIES) {
            gun.shooting(true); // ai always shoots.
        }
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

    private void assembleEnemy(float x, float y, ShipData shipData) {
        int gracepaddingX = 2;
        int gracepaddingY = 0;
        E enemyShip = E()
                .pos(x, y)
                .physics()
                .physicsFriction(0)
                .mortal()
                .shipData(shipData)
                //.physicsVr(50)
                .angle()
                .deadly()
                .flightPatternData(flightPatternDataSystem.get(shipData.flight))
                .teamTeam(TEAM_ENEMIES)
                .render(G.LAYER_GREMLIN + shipData.layerOffset)
                .shieldHp(shipData.hp)
                .origin(shipData.originX, shipData.originY)
                .flying()
                .frozen()
                .anim(shipData.anim);

        if ("boss".equals(shipData.id)) {
            enemyShip.tag("boss");
        }

        gameScreenAssetSystem.boundToAnim(enemyShip.id(), gracepaddingX, gracepaddingY);
        enemyShip.pos(x - enemyShip.boundsCx(), y - enemyShip.boundsCy());

        addArsenal(enemyShip, "enemy-guns", G.TEAM_ENEMIES, -180, shipData.arsenal, true);
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
