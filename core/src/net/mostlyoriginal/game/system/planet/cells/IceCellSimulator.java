package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class IceCellSimulator implements CellSimulator {
    @Override
    public void process(CellDecorator c, float delta) {
        final int temperature = c.mask().temperature;
        if ( temperature > 0 && MathUtils.random(0,100) < temperature ) {
            c.setNextType(PlanetCell.CellType.WATER);
        }
        c.cell.color = Color.rgba8888(MathUtils.random(0.6f, 1f), MathUtils.random(0.6f, 1f), MathUtils.random(0.6f, 1f),  1f);

    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        if ( c.mask().temperature > -30) {
//            c.mask().temperature--;
        }
    }

}
