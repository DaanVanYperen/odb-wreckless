package net.mostlyoriginal.game.system.planet;

import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class PlanetData {

    public String id;
    public String comment; // not used.
    public String texture;
    public CellType[] types;

    public static class CellType {
        String color;
        PlanetCell.CellType type;
        public int intColor;
    }
}
