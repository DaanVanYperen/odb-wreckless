package net.mostlyoriginal.game.system.dilemma;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.AchievementSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
import net.mostlyoriginal.game.system.stencil.PlanetStencilSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.JamOperationFactory.moveBetween;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class CardScriptSystem extends FluidIteratingSystem {


    public CardScriptSystem() {
        super(Aspect.all(CardScript.class));
    }

    PlanetStencilSystem planetStencilSystem;
    PlanetCreationSystem planetCreationSystem;
    GameScreenAssetSystem assetSystem;
    AchievementSystem achievementSystem;
    CardSystem cardSystem;

    int statusIndex = 0;

    @Override
    protected void initialize() {
        super.initialize();
        spawnSkyscrapers();
    }

    public void reset() {
        IntBag cards = world.getAspectSubscriptionManager().get(Aspect.all(StatusEffect.class)).getEntities();
        int[] ids = cards.getData();
        for (int i = 0, s = cards.size(); s > i; i++) {
            E.E(ids[i]).deleteFromWorld();
        }
    }

    @Override
    protected void process(E e) {
        CardData cardData = e.getPlayableCard().card;
        String sfx = cardData.sfx;
        if (sfx != null) {
            assetSystem.playSfx(sfx);
        }

        if (cardData.statuseffect) {
            String cardGfx = "card" + cardData.id;
            addStatusEffect(cardGfx);
        }

        run(e.cardScriptScript());
        e.deleteFromWorld();
    }

    private void addStatusEffect(String cardGfx) {
        E.E().anim(cardGfx)
                .clickable()
                .statusEffect()
                .angleRotation(10f)
                .pos(
                        33 + ((statusIndex % 2) * 40),
                        G.SCREEN_HEIGHT / G.CAMERA_ZOOM - 90 - (statusIndex * 40))
                .tint(1f, 1f, 1f, 0.8f)
                .scale(0.8f)
                .renderLayer(G.LAYER_CARDS + statusIndex);
        statusIndex++;
    }

    private void run(ScriptCommand[] commands) {
        for (ScriptCommand c : commands) {
            runScriptStep(c);
        }
    }

    private void runScriptStep(ScriptCommand c) {
        switch (c) {
            case LAVA_TENDRIL:
                planetStencilSystem.stencilRotateCenter("AIRPOCKET");
                break;
            case HOLLOWEARTH:
                planetStencilSystem.stencil("HOLLOWEARTH");
                break;
            case WATERWORLD:
                planetStencilSystem.stencil("WATERWORLD");
                break;
            case KILL_DUDES:
                killDudes();
            case ADD_DUDES:
                spawnDudes();
                break;
            case LAVA_PRESSURE_UP:
                getPlanet().lavaPressure += 1000;
                break;
            case WATER_PRESSURE_UP:
                getPlanet().waterPressure += 3000;
                break;
            case ANGRY_RANDOMIZE:
                randomizeAnger();
                break;
            case ANGRY_NONE:
                killAnger();
                break;
            case DOLPHINIZE:
                randomizeDolphin();
                break;
            case RESTART:
                restartGame();
                break;
            case CURE_FOR_DEATH:
                innoculateEveryoneForDeath();
                break;
            case SPAWN_ICBMS:
                spawnIcbms();
                break;
            case SPAWN_GALAXYBUCKS:
                spawnStarbucks();
                break;
            case SPAWN_ACCELERATOR:
                spawnAccelerators();
                break;
            case SPAWN_STRUCTURES:
                spawnSkyscrapers();
                break;

        }
    }

    private void spawnIcbms() {
        spawnStructure(2, 4, "icbm", G.LAYER_STRUCTURES_FOREGROUND);
    }

    private void spawnSkyscrapers() {
        for (int i = 0; i < MathUtils.random(10, 20); i++) {
            spawnStructure("skyscraper" + MathUtils.random(7), G.LAYER_STRUCTURES_BACKGROUND);
        }

    }

    private void spawnAccelerators() {
        spawnStructure("spaceship", G.LAYER_STRUCTURES_FOREGROUND);
        spawnStructure("alien", G.LAYER_STRUCTURES_FOREGROUND);
    }

    private void spawnStarbucks() {
        spawnStructure(2, 5, "galaxybucks", G.LAYER_STRUCTURES);
    }

    private void spawnStructure(int min, int max, String id, int layer) {
        for (int i = 0; i < MathUtils.random(min, max); i++) {
            spawnStructure(id, layer);
        }
    }

    private void spawnStructure(String id, int layer) {
        Vector2 location = planetCreationSystem.getSpawnLocation();
        E.E()
                .pos(location.x, location.y)
                .anim(id)
                .originX(0.5f)
                .originY(0.1f)
                .renderLayer(layer)
                .angle()
                .physics()
                .planetbound()
                .flammable()
                .tint(Tint.TRANSPARENT)
                .script(sequence(delay(MathUtils.random(0.1f, 0.4f)), add(new Mass(1.1f)), tween(Tint.TRANSPARENT, Tint.WHITE, 0.2f)))
                .orientToGravityIgnoreFloor(true);
    }

    private void innoculateEveryoneForDeath() {
        IntBag entities = getWanderers();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            e.reviveAfterDeath();
        }
    }

    private void restartGame() {
        planetCreationSystem.restart();
        reset();
        achievementSystem.gameEnded = false;
    }

    private void killDudes() {
        IntBag entities = getWanderers();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            e.removeDolphinized();
            if (MathUtils.randomBoolean()) {
                e.died();
            }
        }
    }

    private void spawnDudes() {
        for (int i = 0; i < 5; i++) {
            planetCreationSystem.spawnDude(MathUtils.random(0, 360));
        }
    }

    Vector2 v = new Vector2();

    private void randomizeDolphin() {
        IntBag entities = getWanderers();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            Vector2 gravityVector = v.set(e.planetboundGravity()).rotate(180f);
            e
                    .dolphinized()
                    .physicsVr(1000f)
                    .physicsVx(gravityVector.x * 100f)
                    .physicsVy(gravityVector.y * 100f);
        }
    }

    private void randomizeAnger() {
        IntBag entities = getWanderers();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            e.removeDolphinized();
            if (MathUtils.randomBoolean()) {
                e.removeAngry();
            } else {
                e.angry();
            }
        }
    }

    private void killAnger() {
        IntBag entities = getWanderers();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            e.removeAngry();
        }
    }

    private IntBag getWanderers() {
        return world.getAspectSubscriptionManager().get(Aspect.all(Wander.class)).getEntities();
    }

    private Planet getPlanet() {
        return planetCreationSystem.planetEntity.getPlanet();
    }
}
