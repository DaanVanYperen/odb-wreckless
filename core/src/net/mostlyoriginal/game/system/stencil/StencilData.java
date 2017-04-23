package net.mostlyoriginal.game.system.stencil;

import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class StencilData {
    public String id;
    public String comment; // not used.
    public String texture;

    public PlanetCell.CellType[] replaceTypes;
}
