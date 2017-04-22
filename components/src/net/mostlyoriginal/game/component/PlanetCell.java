package net.mostlyoriginal.game.component;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Daan van Yperen
 */
public class PlanetCell {
    public int x;
    public int y;
    public int color = 0;
    public CellType type = CellType.STATIC;

    public enum CellType {
        STATIC,
        LAVA
    }
}
