package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class AirCellSimulator implements CellSimulator {

    @Override
    public void process(CellDecorator c, float delta) {
        c.cell.color = Color.rgba8888(0f, MathUtils.random(0.6f, 0.7f), MathUtils.random(0.6f, 1f), 1f);

        if (swapIfSwappable(c, c.getNeighbourAbove())) {
            return;
        }
        if (swapIfSwappable(c, c.getNeighbourAboveL())) {
            return;
        }
        if (swapIfSwappable(c, c.getNeighbourAboveR())) {
            return;
        }

        if (MathUtils.randomBoolean()) {
            swapWithBestWater(c, c.getNeighbourLeft());
        } else {
            swapWithBestWater(c, c.getNeighbourRight());
        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }

    private boolean swapWithBestWater(CellDecorator c, PlanetCell neighbourLeft) {
        if (neighbourLeft != null && isFluid(neighbourLeft)) {
            CellDecorator child = c.forChild(neighbourLeft);
            PlanetCell aboveChild = child.getNeighbourAbove();
            if (aboveChild != null && isFluid(aboveChild)) {
                c.swapWith(aboveChild);
                return true;
            } else {
                c.swapWith(neighbourLeft);
                return true;
            }
        }
        return false;
    }

    private boolean isFluid(PlanetCell neighbourLeft) {
        return neighbourLeft.type == PlanetCell.CellType.WATER || neighbourLeft.type == PlanetCell.CellType.LAVA;
    }

    private boolean swapIfSwappable(CellDecorator c, PlanetCell neighbourAbove) {
        if (neighbourAbove != null && neighbourAbove.type != PlanetCell.CellType.AIR && neighbourAbove.type != PlanetCell.CellType.STATIC) {
            c.swapWith(neighbourAbove);
            return true;
        }
        return false;
    }
}
