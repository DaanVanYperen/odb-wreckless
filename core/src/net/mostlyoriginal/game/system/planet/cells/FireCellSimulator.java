package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class FireCellSimulator implements CellSimulator {

    Color coldColor;
    Color aridColor;
    Color workColor = new Color();

    @Override
    public void color(CellDecorator c, float delta) {
        if (aridColor == null) {
            coldColor = new Color();
            aridColor = new Color();
            Color.rgba8888ToColor(coldColor, c.planet.cellColor[PlanetCell.CellType.FIRE.ordinal()]);
            Color.rgba8888ToColor(aridColor, c.planet.cellColorSecondary[PlanetCell.CellType.FIRE.ordinal()]);
            coldColor.a = aridColor.a = 1f;
        }

        float a = MathUtils.random(0f, 1f);
        workColor.r = Interpolation.linear.apply(coldColor.r, aridColor.r, a);
        workColor.g = Interpolation.linear.apply(coldColor.g, aridColor.g, a);
        workColor.b = Interpolation.linear.apply(coldColor.b, aridColor.b, a);
        workColor.a = 1f;
        c.cell.color = Color.rgba8888(workColor);
    }

    @Override
    public void process(CellDecorator c, float delta) {

        if (c.cell.nextType == null) {
            if (MathUtils.random(0, 100) < 1f) {
                if (MathUtils.random(0, 100) < 50) {
                    c.setNextType(PlanetCell.CellType.STEAM);
                } else {
                    c.setNextType(PlanetCell.CellType.AIR);
                }
            }
        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        if (c.mask().temperature < 20) {
            c.mask().temperature++;
        }
    }

    private boolean isFlows(PlanetCell neighbourLeft) {
        return neighbourLeft.type.flows();
    }
}
