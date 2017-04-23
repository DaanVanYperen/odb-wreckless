package net.mostlyoriginal.game.system.planet.cells;

/**
 * @author Daan van Yperen
 */
public interface CellSimulator {
    void color(CellDecorator c, float delta);
    void process(CellDecorator c, float delta);
    void updateMask(CellDecorator c, float delta);
}
