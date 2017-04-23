package net.mostlyoriginal.game.system.planet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for all cards.
 */
public class PlanetLibrary {
    public PlanetData[] planets;

    public PlanetLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public PlanetData getById(String id) {
        for (PlanetData planet : planets) {
            if (planet.id != null && planet.id.equals(id)) return planet;
        }
        return null;
    }
}
