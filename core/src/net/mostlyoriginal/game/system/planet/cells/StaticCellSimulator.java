package net.mostlyoriginal.game.system.planet.cells;

/**
 * @author Daan van Yperen
 */
public class StaticCellSimulator implements CellSimulator {
    @Override
    public void color(CellDecorator c, float delta) {
        //c.cell.color = c.planet.cellColor[c.cell.type.ordinal()];
    }

    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.sleep=4;
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }

}
