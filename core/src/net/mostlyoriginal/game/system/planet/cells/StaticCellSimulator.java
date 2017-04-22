package net.mostlyoriginal.game.system.planet.cells;

/**
 * @author Daan van Yperen
 */
public class StaticCellSimulator implements CellSimulator {
    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.sleep=2;
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }

}
