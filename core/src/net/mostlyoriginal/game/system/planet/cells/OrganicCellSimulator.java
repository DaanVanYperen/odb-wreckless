package net.mostlyoriginal.game.system.planet.cells;

import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;

/**
 * @author Daan van Yperen
 */
public class OrganicCellSimulator implements CellSimulator {
    @Override
    public void color(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[c.cell.type.ordinal()];
    }

    @Override
    public void process(CellDecorator c, float delta) {
        // spread.
        PlanetCell target = c.getRandomNeighbour(PlanetCell.CellType.AIR, 3);
        if (target != null && target.nextType == null && target.type == PlanetCell.CellType.AIR) {
            CellDecorator proxies = c.proxies(target);
            if (proxies.getNeighbour(PlanetCell.CellType.STATIC) != null) {
                target.nextType = PlanetCell.CellType.ORGANIC;
                target.nextColor = c.planet.cellColor[PlanetCell.CellType.ORGANIC.ordinal()];
            } else {
                // parent on dirt?
                if (c.getNeighbour(PlanetCell.CellType.STATIC) != null || FauxRng.random(100) < 5 ) {
                    target.nextType = PlanetCell.CellType.ORGANIC;
                    target.nextColor = c.planet.cellColor[PlanetCell.CellType.ORGANIC.ordinal()];
                }
            }
        }

        if (c.getNeighbour(PlanetCell.CellType.FIRE, PlanetCell.CellType.LAVA, PlanetCell.CellType.LAVA_CRUST) != null) {
            c.cell.nextType = PlanetCell.CellType.FIRE;
        }

        // die if no air.
        if (c.getNeighbour(PlanetCell.CellType.AIR) == null) {
            c.cell.nextType = PlanetCell.CellType.AIR;
        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
    }
}
