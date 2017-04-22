package net.mostlyoriginal.game.component;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Daan van Yperen
 */
public class PlanetCell {
    public int x;
    public int y;
    public int color = 0;
    public int down = -1;
    public CellType type = CellType.STATIC;


    public CellType nextType = null;
    public int nextColor = -1;

    public static int[][] directions = {
            {-1, -1},
            {0, -1},
            {1, -1},
            {1, 0},
            {1, 1},
            {0, 1},
            {-1, 1},
            {-1, 0}
    };

    public int up() {
        return (down + 4) % 8;
    }
    public int left() {
        return (down + 6) % 8;
    }

    public int right() {
        return (down + 2) % 8;
    }

    public int upL() {
        return (down + 3) % 8;
    }

    public int upR() {
        return (down + 5) % 8;
    }

    public void activateNextType() {
        if ( nextType != null ) { type = nextType; nextType = null; }
        if ( nextColor != -1 ) { color = nextColor; nextColor = -1; }
    }

    public enum CellType {
        STATIC,
        LAVA,
        WATER,
        AIR, ICE
    }
}
