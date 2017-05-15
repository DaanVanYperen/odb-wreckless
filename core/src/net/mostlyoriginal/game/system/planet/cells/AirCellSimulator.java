package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;


/**
 * @author Daan van Yperen
 */
public class AirCellSimulator implements CellSimulator {

    public static final float AIR_FADE_BAND_WIDTH = 50f;
    Color coldColor;
    Color aridColor;
    Color workColor = new Color();

    @Override
    public void color(CellDecorator c, float delta) {
        if (FauxRng.random(100) < 40) { // expensive, so run less often!
            if (aridColor == null) {
                coldColor = new Color();
                aridColor = new Color();

                Color.rgba8888ToColor(coldColor, c.planet.cellColor[PlanetCell.CellType.AIR.ordinal()]);
                Color.rgba8888ToColor(aridColor, c.planet.cellColorArid[PlanetCell.CellType.AIR.ordinal()]);
                coldColor.a = aridColor.a = 1f;
            }

            float a = MathUtils.clamp((c.mask().temperature / StatusMask.ARID_TEMPERATURE), 0f, 1f);
            workColor.r = Interpolation.linear.apply(coldColor.r, aridColor.r, a);
            workColor.g = Interpolation.linear.apply(coldColor.g, aridColor.g, a);
            workColor.b = Interpolation.linear.apply(coldColor.b, aridColor.b, a);
            workColor.a = c.cell.depth() < AIR_FADE_BAND_WIDTH ? MathUtils.clamp(c.cell.depth() / AIR_FADE_BAND_WIDTH, 0.05f, 1f) : 1f;

            c.cell.color = Color.rgba8888(workColor);
        }
    }

    @Override
    public void process(CellDecorator c, float delta) {

        if (c.cell.nextType != null ) return;

        if (swapIfSwappable(c, c.getNeighbourAbove())) {
            return;
        }
        if (swapIfSwappable(c, c.getNeighbourAboveL())) {
            return;
        }
        if (swapIfSwappable(c, c.getNeighbourAboveR())) {
            return;
        }

        if (FauxRng.randomBoolean()) {
            if (c.swapWithBestFlowing(c.getNeighbourLeft())) return;
        } else {
            if (c.swapWithBestFlowing(c.getNeighbourRight())) return;
        }

        c.cell.sleep = 1;
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {

    }

    private boolean swapIfSwappable(CellDecorator c, PlanetCell neighbourAbove) {
        if (neighbourAbove != null && neighbourAbove.type.density != null && neighbourAbove.type.isLighter(c.cell.type)) {
            c.swapWith(neighbourAbove);
            return true;
        }
        return false;
    }
}
