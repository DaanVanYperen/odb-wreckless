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
        c.cell.color = Color.rgba8888( MathUtils.random(0.6f, 1f), c.countNeighbour(PlanetCell.CellType.STATIC) > 0 ? 0.5f : 0,0, 1f);
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        StatusMask mask = c.mask();
        if (mask.temperature < 300 ) {
            mask.temperature ++;
        }
    }
}
