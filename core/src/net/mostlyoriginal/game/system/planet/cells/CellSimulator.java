package net.mostlyoriginal.game.system.planet.cells;

/**
 * @author Daan van Yperen
 */
public interface CellSimulator {
    void process(CellDecorator c, float delta);
}
