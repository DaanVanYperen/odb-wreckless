package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.TowedSystem;
import net.mostlyoriginal.game.system.detection.ScoreUISystem;
import net.mostlyoriginal.game.system.view.*;

import static com.artemis.E.E;
import static net.mostlyoriginal.api.operation.OperationFactory.delay;
import static net.mostlyoriginal.api.operation.OperationFactory.deleteFromWorld;
import static net.mostlyoriginal.api.operation.OperationFactory.sequence;
import static net.mostlyoriginal.api.utils.Duration.milliseconds;
import static net.mostlyoriginal.api.utils.Duration.seconds;
import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
ligh */
public class EntitySpawnerSystem extends BaseSystem {

    //    private SocketSystem socketSystem;
//    private PowerSystem powerSystem;
//    private SpoutSystem spoutSystem;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private ShipDataSystem shipDataSystem;
    private TowedSystem towedSystem;
    private float raceStartingCooldown;

    @Override
    protected void processSystem() {
        raceStartingCooldown -= world.delta;
    }

    public boolean raceStarted() {
        return raceStartingCooldown <= 0;
    }


    public boolean spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");


        switch (entity) {
            case "player":
                assemblePlayer(x, y, shipDataSystem.get("player"), (boolean)properties.get("tutorial"));
                break;
            case "startinglights":
                assembleStartingLights((int) x, (int) y);
                return true;
            case "startinggrid":
                assembleRacer((int) x, (int) y, ChainColor.random().name(), seconds(5 + 4));
                return false;
            case "car":
                assembleCar((int) x, (int) y, (String) properties.get("color"));
                break;
            case "pitstop":
                assemblePitstop((int) x, (int) y, (String) properties.get("color"), (Integer) properties.get("multiplier"));
                return false;
            case "control":
                assembleControl((int) x, (int) y, (String) properties.get("type"));
                return false;
            case "display":
                assembleDisplay((int) x, (int) y, (String) properties.get("show"));
                break;
            case "hazard":
                assembleHazard((int) x, (int) y,
                        (Boolean) properties.get("down"),
                        (String) properties.get("sprite-up"),
                        (String) properties.get("sprite-down"),
                        (String) properties.get("hitSound"));
                break;
            case "oilslick":
                assembleOilslick((int) x, (int) y, (String) properties.get("anim"));
                return false;
            case "trigger":
                assembleTrigger(x, y, (String) properties.get("trigger"), (Integer) properties.get("tolevel"));
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
                return false;
            //throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
        return true;
    }

    private void assembleDisplay(int x, int y, String s) {

        Preferences prefs = Gdx.app.getPreferences("ld41wrecklessR2");
        final int score = prefs.getInteger(s, 0);

        E e = E().pos(x,y+32)
                .render(G.LAYER_GREMLIN-20)
                .fontFontName("ital")
                .labelText(score == 0 ? "No highscore" : "Highscore: " + ScoreUISystem.getDecimalFormattedString(Integer.toString(score)));

    }

    private void assembleControl(int x, int y, String type) {
        final E e = E()
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .bounds(0,0,G.CELL_SIZE,G.CELL_SIZE)
                .teamTeam(TEAM_ENEMIES)
                .inputsDown("down".equals(type))
                .inputsLeft("left".equals(type))
                .inputsRight("right".equals(type))
                .inputsUp("up".equals(type))
                .inputsJustFire("e".equals(type))
                .inputsFire("holde".equals(type))
                .frozen();
    }

    private void assembleStartingLights(int x, int y) {
        G.sfx.play("countdown_3");
        final E e = E().pos(x - 160 / 2, y - 128 / 2)
                .anim("startinglightss")
                .renderLayer(G.LAYER_DIALOGS)
                .script(sequence(delay(milliseconds(4000)), deleteFromWorld()));
        raceStartingCooldown = 4.2f;
        gameScreenAssetSystem.boundToAnim(e.id(), 4, 4);

    }

    private void assembleOilslick(int x, int y, String anim) {
        final E e = E().pos(x, y)
                .crashable()
                .bounds(10, 10, 20, 20)
                .frozen()
                .oilslick();

        if ( anim != null ) {
            e.anim(anim).renderLayer(G.LAYER_GREMLIN-6);
        }
    }

    private void assembleHazard(int x, int y, Boolean down, String spriteUp, String spriteDown, String hitSound) {
        final E e = E().pos(x, y)
                .angleRotation(down ? MathUtils.random(0f, 360f) : 0f)
                .crashable()
                .hazardDown(down)
                .hazardSpriteDown(spriteDown)
                .hazardSpriteUp(spriteUp)
                .hazardHitSound(hitSound)
                .origin(0.5f, 0.5f)
                .tint(1f, 1f, 1f, 0.7f)
                .frozen()
                .anim(down ? spriteDown : spriteUp)
                .renderLayer(G.LAYER_GREMLIN - 5);

        if ("frogger".equals(spriteUp)) {
            e
                    .snapToGridX(x / G.CELL_SIZE)
                    .snapToGridPixelsPerSecondY(60)
                    .tint(1f,1f,1f,1f)
                    .snapToGridY(20);
        }
        gameScreenAssetSystem.boundToAnim(e.id(), 4, 4);
    }

    public E assembleCar(int x, int y, String color) {
        final E e = E()
                .pos(x, y)
                .angle()
                .origin(0.5f, 0.5f)
                .render(G.LAYER_GREMLIN + MathUtils.random(G.LAYERS_RESERVED_FOR_CARS))
                .snapToGrid()
                .angleRotation(MathUtils.random(0, 360))
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

    public E assembleRacer(int x, int y, String color, float fadeDelay) {
        final E e = E()
                .pos(x, y)
                .angle()
                .origin(0.5f, 0.5f)
                .render(G.LAYER_GREMLIN + MathUtils.random(G.LAYERS_RESERVED_FOR_CARS))
                .crashable()
                .snapToGrid()
                .tireTrack()
                .teamTeam(TEAM_ENEMIES)
                .tint(1f, 1f, 1f, 0.9f)
                .chainableColor(ChainColor.valueOf(color))
                .snapToGridX(x / G.CELL_SIZE + G.CELL_SIZE * 100)
                .snapToGridY(y / G.CELL_SIZE)
                .snapToGridPixelsPerSecondX((int) (MathUtils.random(250f, 310f) * 0.9))
                .script(sequence(
                        delay(fadeDelay),
                        deleteFromWorld()
                ))
                .anim("car-" + color);
        gameScreenAssetSystem.boundToAnim(e.id(), 0, 0);
        return e;


    }

    private E assemblePitstop(int x, int y, String color, Integer multiplier) {
        final E e = E()
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .teamTeam(TEAM_ENEMIES)
                .chainableColor(ChainColor.valueOf(color))
                .frozen()
                .chainableMultiplier(multiplier != null ? multiplier : 1)
                .chainablePitstop(true).bounds(0, 0, G.CELL_SIZE, G.CELL_SIZE);
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

    private void assemblePlayer(float x, float y, ShipData shipData, boolean mode) {
        int gracepaddingX = 16;
        int gracepaddingY = 4;
        E playerCar = E().anim("player-idle")
                .pos(x - 14, y)
                .origin(0.5f, 0.5f)
                .render(G.LAYER_PLAYER)
                .snapToGridX((int) x / G.CELL_SIZE)
                .snapToGridY((int) y / G.CELL_SIZE)
                .snapToGridPixelsPerSecondX(196)
                .mortal()
                .crashable()
                .tireTrack()
                //.gravity()
                .wallSensor()
                .player()
                .teamTeam(G.TEAM_PLAYERS)
                .tag("player")
                .shipControlled()
                .shipControlledTutorial(mode);

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
                .physicsVx(0)
                .tag("camera")
                .ethereal(true)
                .physicsFriction(0);

    }

    private void assembleTrigger(float x, float y, String trigger, Integer parameter) {
        boolean tallTrigger = false;
        E().pos(x, y - (tallTrigger ? 5000 : 0)).bounds(0, 0, 32, (tallTrigger ? 10000 : 32)).trigger(trigger).triggerParameter(""+parameter);
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
