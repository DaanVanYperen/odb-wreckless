package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planetbound;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;

/**
 * @author Daan van Yperen
 */
public class PlanetCoordSystem extends FluidIteratingSystem {
    private PlanetCreationSystem planetCreationSystem;

    public PlanetCoordSystem() {
        super(Aspect.all(Pos.class, Planetbound.class));
    }

    @Override
    protected void process(E e) {
        updateCoord(e);
    }

    public void updateCoord(E e) {
        E planet = planetCreationSystem.planetEntity;
        e.planetboundCell(planet.getPlanet().get((int) (e.posX() - G.PLANET_X)+2, (int) (e.posY() - G.PLANET_Y)));
        e.getPlanetbound().gravity.set(G.PLANET_CENTER_X, G.PLANET_CENTER_Y).sub(e.posX(), e.posY()).nor();
    }
}
