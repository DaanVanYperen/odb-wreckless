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
                // self on dirt? growww!
                target.nextType = PlanetCell.CellType.ORGANIC;
                target.nextColor = c.planet.cellColor[PlanetCell.CellType.ORGANIC.ordinal()];
            } else {
                // parent on dirt? or random chance of growth? then we growww!
                if (c.getNeighbour(PlanetCell.CellType.STATIC) != null || FauxRng.random(2000) < 5 ) {
                    target.nextType = PlanetCell.CellType.ORGANIC;
                    target.nextColor = c.planet.cellColor[PlanetCell.CellType.ORGANIC.ordinal()];
                }
            }
        }

        if (c.getNeighbour(PlanetCell.CellType.FIRE, PlanetCell.CellType.LAVA, PlanetCell.CellType.LAVA_CRUST) != null) {
            c.cell.nextType = PlanetCell.CellType.FIRE;
        }

        // die if no air.
        if (FauxRng.random(100 ) < 25 && c.getNeighbour(PlanetCell.CellType.AIR) == null) {
            c.cell.nextType = PlanetCell.CellType.AIR;
        } else {
            if ( FauxRng.random(1000 ) < 2 && c.countNeighbour(PlanetCell.CellType.AIR) > 6 ) {
                c.cell.nextType = PlanetCell.CellType.ORGANIC_SPORE;
            }

        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
    }
}
