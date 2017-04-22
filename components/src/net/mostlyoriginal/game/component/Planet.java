package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Planet extends Component {

    public static final int SIMULATION_WIDTH = 800;
    public static final int SIMULATION_HEIGHT = 450;
    public static final int GRADIENT_SCALE = 5;

    public Planet() {
    }

    public PlanetCell[][] grid = new PlanetCell[SIMULATION_HEIGHT][SIMULATION_WIDTH];
    public StatusMask[][] mask = new StatusMask[SIMULATION_HEIGHT/ GRADIENT_SCALE][SIMULATION_WIDTH / GRADIENT_SCALE];
    public StatusMask[][] tempMask = new StatusMask[SIMULATION_HEIGHT/ GRADIENT_SCALE][SIMULATION_WIDTH / GRADIENT_SCALE];

    public PlanetCell get(int x, int y) {
        if ( x < 0 || y < 0 || x >= SIMULATION_WIDTH || y >= SIMULATION_HEIGHT ) return null;
        return grid[y][x];
    }

    public StatusMask getStatusMask(int x, int y) {
        if ( x < 0 || y < 0 || x >= SIMULATION_WIDTH || y >= SIMULATION_HEIGHT ) return null;
        return mask[y / GRADIENT_SCALE][x / GRADIENT_SCALE];
    }
}
