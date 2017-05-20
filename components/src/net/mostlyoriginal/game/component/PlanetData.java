package net.mostlyoriginal.game.component;

import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class PlanetData {

    public String id;
    public String comment; // not used.
    public String texture;
    public String backgroundTexture;
    public String dirtTexture;
    public CellType[] types;
    public String spaceBackgroundColor;

    public static class CellType {
        public String color;
        public String colorSecondary;
        public PlanetCell.CellType type;
        public int intColor;
        public int intColorSecondary;
    }
}
