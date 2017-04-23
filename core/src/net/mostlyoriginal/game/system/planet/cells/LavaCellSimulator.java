package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;

/**
 * @author Daan van Yperen
 */
public class LavaCellSimulator implements CellSimulator {
    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[c.cell.type.ordinal()];

        // releave pressure.
        if (c.cell.nextType == null) {
            if (c.planet.lavaPressure > 0) {
                if (attemptReleavePressure(c, c.getNeighbourDown())) return;
                boolean b = MathUtils.randomBoolean(); // flip direction randomly.
                if (attemptReleavePressure(c, b ? c.getNeighbourLeft() : c.getNeighbourRight())) return;
                if (attemptReleavePressure(c, !b ? c.getNeighbourLeft() : c.getNeighbourRight())) return;
                if (attemptReleavePressure(c, b ? c.getNeighbourAboveL() : c.getNeighbourAboveR())) return;
                if (attemptReleavePressure(c, !b ? c.getNeighbourAboveL() : c.getNeighbourAboveR())) return;
                if (attemptReleavePressure(c, c.getNeighbour(PlanetCell.CellType.AIR))) return;
            }
        }

        // crust or not!
        if (c.cell.nextType == null) {
            int nonLavaNeighbours = c.countNonLavaNeighbour();
            if (c.cell.type == PlanetCell.CellType.LAVA_CRUST && nonLavaNeighbours == 0) {
                c.setNextType(PlanetCell.CellType.LAVA);
            }
            if (c.cell.type == PlanetCell.CellType.LAVA && nonLavaNeighbours > 0) {
                c.setNextType(PlanetCell.CellType.LAVA_CRUST);
            }
        }
        if (c.cell.nextType == null) {
            c.flow();
        }
    }

    private boolean attemptReleavePressure(CellDecorator c, PlanetCell neighbour) {
        if (neighbour != null && neighbour.nextType == null && neighbour.type == PlanetCell.CellType.AIR) {
            neighbour.nextType = PlanetCell.CellType.LAVA;
            c.planet.lavaPressure--;
            return true;
        }
        return false;
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        StatusMask mask = c.mask();
        if (mask.temperature < StatusMask.MAX_TEMPERATURE) {
            mask.temperature+=12;
        }
    }
}
