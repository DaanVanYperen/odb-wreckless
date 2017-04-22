package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.cells.*;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class PlanetSimulationSystem extends FluidIteratingSystem {

    private CellSimulator[] simulators = new CellSimulator[PlanetCell.CellType.values().length];

    public PlanetSimulationSystem() {
        super(Aspect.all(Planet.class));
    }

    @Override
    protected void initialize() {
        super.initialize();

        addSimulator(PlanetCell.CellType.STATIC, new StaticCellSimulator());
        addSimulator(PlanetCell.CellType.LAVA, new LavaCellSimulator());
        addSimulator(PlanetCell.CellType.WATER, new WaterCellSimulator());
        addSimulator(PlanetCell.CellType.ICE, new IceCellSimulator());
    }

    private void addSimulator(PlanetCell.CellType cellType, CellSimulator simulator) {
        simulators[cellType.ordinal()] = simulator;
    }

    @Override
    protected void process(E e) {
        final Planet planet = e.getPlanet();
        final float delta = world.delta;
        for (int y = 0; y < Planet.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < Planet.SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                simulators[cell.type.ordinal()].process(cell, delta);
            }
        }
    }
}
