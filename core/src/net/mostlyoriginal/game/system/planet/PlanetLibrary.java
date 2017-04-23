package net.mostlyoriginal.game.system.planet;

/**
 * Repository for all cards.
 */
public class PlanetLibrary {
    public net.mostlyoriginal.game.component.PlanetData[] planets;

    public PlanetLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public net.mostlyoriginal.game.component.PlanetData getById(String id) {
        for (net.mostlyoriginal.game.component.PlanetData planet : planets) {
            if (planet.id != null && planet.id.equals(id)) return planet;
        }
        return null;
    }
}
