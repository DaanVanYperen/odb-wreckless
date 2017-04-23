package net.mostlyoriginal.game.component;

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
        public String color;
        public PlanetCell.CellType type;
        public int intColor;
    }
}
