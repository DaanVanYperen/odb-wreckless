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
        final float temperature = c.mask().temperature;
        if ( temperature > 0 && MathUtils.random(0,100) < temperature ) {
            c.setNextType(PlanetCell.CellType.WATER);
        }
        c.cell.color = c.planet.cellColor[PlanetCell.CellType.ICE.ordinal()];
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        if ( c.mask().temperature > -30) {
//            c.mask().temperature--;
        }
    }

}
