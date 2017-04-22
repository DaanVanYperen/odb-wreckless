package net.mostlyoriginal.game.system.planet.cells;

import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class CellDecorator {
    public PlanetCell cell;
    public final Planet planet;

    public CellDecorator(Planet planet) {
        this.planet = planet;
    }

    public void setColor(int color) {
        cell.color = color;
    }

    public CellDecorator proxies(PlanetCell cell) {
        this.cell = cell;
        return this;
    }

    private static int[][] directions = {
            {-1, -1},
            {0, -1},
            {1, -1},
            {1, 0},
            {1, 1},
            {0, 1},
            {-1, 1},
            {-1, 0}
    };

    public PlanetCell hasNeighbour(PlanetCell.CellType type) {
        for (int i = 0; i < 8; i++) {
            final PlanetCell result = planet.get(cell.x + directions[i][0], cell.y + directions[i][1]);
            if (result != null && result.type == type) return result;
        }
        return null;
    }

    public void setType(PlanetCell.CellType type) {
        cell.type = type;
    }

    public int countNeighbour(PlanetCell.CellType type) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            final PlanetCell result = planet.get(cell.x + directions[i][0], cell.y + directions[i][1]);
            if (result != null && result.type == type) count++;
        }
        return count;
    }
}
