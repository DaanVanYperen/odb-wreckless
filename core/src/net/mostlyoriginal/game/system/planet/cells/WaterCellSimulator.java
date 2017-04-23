package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class WaterCellSimulator implements CellSimulator {
    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[PlanetCell.CellType.WATER.ordinal()];

        if (c.countNeighbour(PlanetCell.CellType.ICE) >= 3) {
            final int temperature = c.mask().temperature;
            if (temperature < 0 && MathUtils.random(0, 100) < -temperature) {
                c.setNextType(PlanetCell.CellType.ICE);
            }
        }

        int lavaCount = c.countNeighbour(PlanetCell.CellType.LAVA) + c.countNeighbour(PlanetCell.CellType.LAVA_CRUST);
        if (lavaCount >= 1) {
            if (MathUtils.random(0, 100) < 25f) {
                // small chance of transforming self.
                if (c.cell.nextType == null) {
                    c.setNextType(PlanetCell.CellType.STEAM);
                }
            } else {
                // small chance of transforming nearby.
                for (int i = 0; i < lavaCount; i++) {
                    PlanetCell planetCell = c.hasNeighbour(PlanetCell.CellType.AIR);
                    if (planetCell == null) planetCell = c.hasNeighbour(PlanetCell.CellType.WATER);
                    if (planetCell != null && planetCell.nextType == null) {
                        c.forChild(planetCell).setNextType(PlanetCell.CellType.STEAM);
                    }
                }
            }
        }

        if (MathUtils.randomBoolean()) {
            c.swapWithBestFlowing(c.getNeighbourLeft());
        } else {
            c.swapWithBestFlowing(c.getNeighbourRight());
        }

    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }
}
