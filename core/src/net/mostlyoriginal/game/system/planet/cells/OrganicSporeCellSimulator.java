package net.mostlyoriginal.game.system.planet.cells;

import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class OrganicSporeCellSimulator implements CellSimulator {
    @Override
    public void color(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[c.cell.type.ordinal()];
    }

    @Override
    public void process(CellDecorator c, float delta) {


        if (c.cell.nextType == null) {
            // die if no air.
            PlanetCell touchingDirt = c.getNeighbour(PlanetCell.CellType.STATIC);
            if (touchingDirt != null) {
                c.cell.nextType = PlanetCell.CellType.ORGANIC;
            } else if (FauxRng.random(1000) < 5) {
                // spores die.
                c.cell.nextType = PlanetCell.CellType.AIR;
            }
        }

        if (c.cell.nextType == null) {
            // spread.
            PlanetCell target = c.getRandomNeighbour(PlanetCell.CellType.AIR, 3);
            if (target != null && target.nextType == null && target.type == PlanetCell.CellType.AIR) {
                c.swapWith(target);
            }

            if (c.getNeighbour(PlanetCell.CellType.FIRE, PlanetCell.CellType.LAVA, PlanetCell.CellType.LAVA_CRUST) != null) {
                c.cell.nextType = PlanetCell.CellType.FIRE;
            }
        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
    }
}
