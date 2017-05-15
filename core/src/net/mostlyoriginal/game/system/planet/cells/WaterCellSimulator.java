package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;

/**
 * @author Daan van Yperen
 */
public class WaterCellSimulator implements CellSimulator {

    public static final int CYAN_COLOR = Color.rgba8888(Color.valueOf("033354"));
    public static final int WATER_ORDINAL = PlanetCell.CellType.WATER.ordinal();

    @Override
    public void color(CellDecorator c, float delta) {
        final int random = FauxRng.random(100);
        if (((c.getNeighbour(PlanetCell.CellType.AIR) != null) && (random < 50)) || (random <= 1)) {
            c.cell.color = CYAN_COLOR;
        } else {
            c.cell.color = c.planet.cellColor[WATER_ORDINAL];
        }
    }

    @Override
    public void process(CellDecorator c, float delta) {
        // releave pressure.
        if (c.cell.nextType == null) {
            if (c.planet.waterPressure > 0 && FauxRng.random(5000) < c.planet.waterPressure) {
                if (attemptReleavePressure(c, c.getNeighbourDown())) return;
                boolean b = FauxRng.randomBoolean(); // flip direction randomly.
                if (attemptReleavePressure(c, b ? c.getNeighbourLeft() : c.getNeighbourRight())) return;
                if (attemptReleavePressure(c, !b ? c.getNeighbourLeft() : c.getNeighbourRight())) return;
                if (attemptReleavePressure(c, b ? c.getNeighbourAboveL() : c.getNeighbourAboveR())) return;
                if (attemptReleavePressure(c, !b ? c.getNeighbourAboveL() : c.getNeighbourAboveR())) return;
                if (attemptReleavePressure(c, c.getNeighbour(PlanetCell.CellType.AIR))) return;
            }

            // Freeze by ice.
            if (c.countNeighbour(PlanetCell.CellType.ICE) >= 3) {
                final float temperature = c.mask().temperature;
                if (temperature < 0 && FauxRng.random(100) < -temperature) {
                    c.setNextType(PlanetCell.CellType.ICE);
                }
            }
        }

        // Turn to steam by lava.
        if (c.getNeighbour(PlanetCell.CellType.LAVA, PlanetCell.CellType.LAVA_CRUST) != null) {
            int lavaCount = c.countNeighbour(PlanetCell.CellType.LAVA, PlanetCell.CellType.LAVA_CRUST);
            if (lavaCount >= 1) {
                if (FauxRng.random(100) < 75f) {
                    // small chance of transforming self.
                    if (c.cell.nextType == null) {
                        c.setNextType(PlanetCell.CellType.STEAM);
                    }
                } else {
                    // small chance of transforming nearby.
                    for (int i = 0; i < lavaCount; i++) {
                        PlanetCell planetCell = c.getNeighbour(PlanetCell.CellType.AIR);
                        if (planetCell == null) planetCell = c.getNeighbour(PlanetCell.CellType.WATER);
                        if (planetCell != null && planetCell.nextType == null) {
                            c.forChild(planetCell).setNextType(PlanetCell.CellType.STEAM);
                        }
                    }
                }
            }
        }

        if (c.cell.nextType == null) {
            final float temperature = c.mask().temperature;
            if (temperature >= StatusMask.ARID_TEMPERATURE && FauxRng.random( 100) < 4) {
                c.setNextType(PlanetCell.CellType.AIR);
            }
        }

        c.flow();
    }

    private boolean attemptReleavePressure(CellDecorator c, PlanetCell neighbour) {
        if (neighbour != null && neighbour.nextType == null && neighbour.type == PlanetCell.CellType.AIR) {
            neighbour.nextType = PlanetCell.CellType.WATER;
            c.planet.waterPressure--;
            return true;
        }
        return false;
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }
}
