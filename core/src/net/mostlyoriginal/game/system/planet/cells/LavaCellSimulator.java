package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class LavaCellSimulator implements CellSimulator {
    @Override
    public void process(PlanetCell planetCell, float delta) {
        planetCell.color = Color.rgba8888(MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), 0, 1f);
    }
}
