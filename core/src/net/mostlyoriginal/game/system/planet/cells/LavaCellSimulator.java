package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;

/**
 * @author Daan van Yperen
 */
public class LavaCellSimulator implements CellSimulator {
    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[c.cell.type.ordinal()];

        if (c.cell.nextType == null) {
            int nonLavaNeighbours = c.countNonLavaNeighbour();
            if (c.cell.type == PlanetCell.CellType.LAVA_CRUST && nonLavaNeighbours == 0) {
                c.setNextType(PlanetCell.CellType.LAVA);
            }
            if (c.cell.type == PlanetCell.CellType.LAVA && nonLavaNeighbours > 0) {
                c.setNextType(PlanetCell.CellType.LAVA_CRUST);
            }
        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        StatusMask mask = c.mask();
        if (mask.temperature < 300) {
            mask.temperature++;
        }
    }
}
