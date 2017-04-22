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

        if (c.countNeighbour(PlanetCell.CellType.ICE) >= 3) {
            final int temperature = c.mask().temperature;
            if (temperature < 0 && MathUtils.random(0, 100) < -temperature) {
                c.setNextType(PlanetCell.CellType.ICE);
            }
        }

        c.cell.color = Color.rgba8888(0f, 0f, MathUtils.random(0.6f, 1f), 1f);
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }
}
