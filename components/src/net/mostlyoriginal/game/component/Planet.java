package net.mostlyoriginal.game.component;

import com.artemis.Component;

import static net.mostlyoriginal.game.component.G.GRADIENT_SCALE;
import static net.mostlyoriginal.game.component.G.SIMULATION_HEIGHT;
import static net.mostlyoriginal.game.component.G.SIMULATION_WIDTH;

/**
 * @author Daan van Yperen
 */
public class Planet extends Component {


    public Planet() {
    }

    public PlanetCell[][] grid = new PlanetCell[SIMULATION_HEIGHT][SIMULATION_WIDTH];
    public StatusMask[][] mask = new StatusMask[SIMULATION_HEIGHT/ GRADIENT_SCALE][SIMULATION_WIDTH / GRADIENT_SCALE];
    public StatusMask[][] tempMask = new StatusMask[SIMULATION_HEIGHT/ GRADIENT_SCALE][SIMULATION_WIDTH / GRADIENT_SCALE];
    public PlanetData data;
    public int lavaPressure = 0;
    public int waterPressure=0;
    public float cooldown = 0;

    public int cellColor[] = new int[PlanetCell.CellType.values().length];
    public int cellColorSecondary[] = new int[PlanetCell.CellType.values().length];

    public PlanetCell get(int x, int y) {
        if ( x < 0 || y < 0 || x >= SIMULATION_WIDTH || y >= SIMULATION_HEIGHT ) return null;
        return grid[y][x];
    }

    public StatusMask getStatusMask(int x, int y) {
        if ( x < 0 || y < 0 || x >= SIMULATION_WIDTH || y >= SIMULATION_HEIGHT ) return null;
        return mask[y / GRADIENT_SCALE][x / GRADIENT_SCALE];
    }
}
