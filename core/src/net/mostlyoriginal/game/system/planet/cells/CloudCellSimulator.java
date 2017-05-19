package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class CloudCellSimulator implements CellSimulator {

    @Override
    public void color(CellDecorator c, float delta) {
        c.cell.color = c.planet.cellColor[PlanetCell.CellType.CLOUD.ordinal()];
    }

    @Override
    public void process(CellDecorator c, float delta) {

        if (c.cell.nextType == null) {
            if (FauxRng.random(1000) < 1) {
                c.cell.nextType = PlanetCell.CellType.WATER;
            }
        }

        if (c.cell.nextType == null) {
            int cloudsNearby =
                    countCloudsAs1(c.getNeighbourRight()) +
                            countCloudsAs1(c.getNeighbourAboveR()) +
                            countCloudsAs1(c.getNeighbourAbove()) +
                            countCloudsAs1(c.getNeighbourDown());

            if (c.cell.nextType == null && FauxRng.random(100) < 5 + (cloudsNearby * 10f)) {
                if (c.cell.depth() > 25) {
                    if (swapWithAirIgnoreSpace(c, c.getNeighbourAbove())) return;
                }
                PlanetCell neighbourLeft = c.getNeighbourLeft();
                if (swapWithAirIgnoreSpace(c,
                        (c.cell.depth() < 15)
                                || (FauxRng.random(100) < 2)
                                || (neighbourLeft != null && neighbourLeft.type == PlanetCell.CellType.CLOUD) ? c.getNeighbourDown() : neighbourLeft)) {
                    return;
                }
            }
        }
    }

    private int countCloudsAs1(PlanetCell cell) {
        return cell != null && cell.type == PlanetCell.CellType.CLOUD ? 1 : 0;
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
    }

    private boolean swapWithAirIgnoreSpace(CellDecorator c, PlanetCell dest) {
        if (dest != null && dest.nextType == null && dest.type == PlanetCell.CellType.AIR) {
            c.swapWith(dest);
            return true;
        }
        return false;
    }
}
