package net.mostlyoriginal.game.system.planet.cells;

import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public interface CellSimulator {
    void process(PlanetCell planetCell, float delta);
}
