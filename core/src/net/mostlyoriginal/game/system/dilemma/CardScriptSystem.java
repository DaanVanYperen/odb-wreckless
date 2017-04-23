package net.mostlyoriginal.game.system.dilemma;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
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
                planetStencilSystem.stencil("AIRPOCKET");
                break;
            case LAVA_PRESSURE_UP:
                getPlanet().lavaPressure += 1000;
                break;
            case ANGRY_RANDOMIZE:
                randomizeAnger();
                break;
        }
    }

    private void randomizeAnger() {
        IntBag entities = world.getAspectSubscriptionManager().get(Aspect.all(Wander.class)).getEntities();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            if (MathUtils.randomBoolean()) {
                e.removeAngry();
            } else {
                e.angry();
            }
        }
    }

    private Planet getPlanet() {
        return planetCreationSystem.planetEntity.getPlanet();
    }
}
