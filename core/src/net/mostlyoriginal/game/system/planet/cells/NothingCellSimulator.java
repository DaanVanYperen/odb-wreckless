package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class NothingCellSimulator implements CellSimulator {
    @Override
    public void color(CellDecorator c, float delta) {
    }

    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.sleep = 20 + MathUtils.random(0, 2);
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }

}
