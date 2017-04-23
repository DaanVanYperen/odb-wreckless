package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class WaterCellSimulator implements CellSimulator {
    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[PlanetCell.CellType.WATER.ordinal()];

        // Freeze by ice.
        if (c.countNeighbour(PlanetCell.CellType.ICE) >= 3) {
            final float temperature = c.mask().temperature;
            if (temperature < 0 && MathUtils.random(0, 100) < -temperature) {
                c.setNextType(PlanetCell.CellType.ICE);
            }
        }

        // Turn to steam by lava.
        int lavaCount = c.countNeighbour(PlanetCell.CellType.LAVA) + c.countNeighbour(PlanetCell.CellType.LAVA_CRUST);
        if (lavaCount >= 1) {
            if (MathUtils.random(0, 100) < 75f) {
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

        if ( c.cell.nextType == null ) {
            final float temperature = c.mask().temperature;
            if (temperature > 50 && MathUtils.random(0, 100) < 4) {
                c.setNextType(PlanetCell.CellType.AIR);
            }
        }

        c.flow();
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }
}
