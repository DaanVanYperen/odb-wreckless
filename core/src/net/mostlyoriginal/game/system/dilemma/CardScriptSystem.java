package net.mostlyoriginal.game.system.dilemma;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.CardScript;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.ScriptCommand;
import net.mostlyoriginal.game.component.Wander;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
import net.mostlyoriginal.game.system.stencil.PlanetStencilSystem;

/**
 * @author Daan van Yperen
 */
public class CardScriptSystem extends FluidIteratingSystem {
    public CardScriptSystem() {
        super(Aspect.all(CardScript.class));
    }

    PlanetStencilSystem planetStencilSystem;
    PlanetCreationSystem planetCreationSystem;

    @Override
    protected void process(E e) {
        run(e.cardScriptScript());
        e.deleteFromWorld();
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
            case ADD_DUDES:
                spawnDudes();
                break;
            case LAVA_PRESSURE_UP:
                getPlanet().lavaPressure += 1000;
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
            Vector2 gravityVector = v.set(e.planetCoordGravity()).rotate(180f);
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
