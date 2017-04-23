package net.mostlyoriginal.game.system.dilemma;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.game.component.CardScript;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.ScriptCommand;
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
        }
    }

    private Planet getPlanet() {
        return planetCreationSystem.planetEntity.getPlanet();
    }
}
