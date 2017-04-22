package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.cells.*;

import static net.mostlyoriginal.game.component.Planet.GRADIENT_SCALE;
import static net.mostlyoriginal.game.component.Planet.SIMULATION_HEIGHT;
import static net.mostlyoriginal.game.component.Planet.SIMULATION_WIDTH;

/**
 * @author Daan van Yperen
 */
public class PlanetSimulationSystem extends FluidIteratingSystem {

    private CellSimulator[] simulators = new CellSimulator[PlanetCell.CellType.values().length];
    private Vector2 v = new Vector2();

    public PlanetSimulationSystem() {
        super(Aspect.all(Planet.class));
    }

    @Override
    protected void initialize() {
        super.initialize();

        addSimulator(PlanetCell.CellType.STATIC, new StaticCellSimulator());
        addSimulator(PlanetCell.CellType.LAVA, new LavaCellSimulator());
        addSimulator(PlanetCell.CellType.WATER, new WaterCellSimulator());
        addSimulator(PlanetCell.CellType.AIR, new AirCellSimulator());
        addSimulator(PlanetCell.CellType.ICE, new IceCellSimulator());
        addSimulator(PlanetCell.CellType.STEAM, new SteamCellSimulator());
    }

    private void addSimulator(PlanetCell.CellType cellType, CellSimulator simulator) {
        simulators[cellType.ordinal()] = simulator;
    }

    @Override
    protected void process(E e) {
        final Planet planet = e.getPlanet();
        generateMask(planet);
        simulateCells(planet);
        activateChanges(planet);
    }

    private void clearMask(Planet planet) {
        int h = SIMULATION_HEIGHT / GRADIENT_SCALE;
        int w = SIMULATION_WIDTH / GRADIENT_SCALE;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                planet.mask[y][x].reset();
                planet.tempMask[y][x].reset();
                planet.mask[y][x].temperature =

                        (int) MathUtils.clamp((h/4f)- (v.set(w / 2, h / 2).sub(w / 4 + x / 2,  y).len()), -200, 200)/5;
            }
        }
    }

    private void activateChanges(Planet planet) {
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                cell.activateNextType();
            }
        }
    }

    private void simulateCells(Planet planet) {
        CellDecorator planetCell = new CellDecorator(planet);
        final float delta = world.delta;
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                simulators[cell.type.ordinal()].process(planetCell.proxies(cell), delta);
            }
        }
    }

    private void generateMask(Planet planet) {
        clearMask(planet);
        CellDecorator planetCell = new CellDecorator(planet);
        final float delta = world.delta;
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                simulators[cell.type.ordinal()].updateMask(planetCell.proxies(cell), delta);
            }
        }
        smoothMask(planet);
        smoothMask(planet);
        smoothMask(planet);
        smoothMask(planet);
    }

    private void smoothMask(Planet planet) {
        for (int y = 1; y < (SIMULATION_HEIGHT / GRADIENT_SCALE) - 1; y++) {
            for (int x = 1; x < (SIMULATION_WIDTH / GRADIENT_SCALE) - 1; x++) {
                planet.tempMask[y][x].temperature = planet.mask[y][x].temperature;
                for (int i = 0; i < 8; i++) {
                    planet.tempMask[y][x].temperature +=
                            planet.mask[y + PlanetCell.directions[i][1]][x + PlanetCell.directions[i][0]].temperature;
                }
                planet.tempMask[y][x].temperature /= 4;
            }
        }
        for (int y = 1; y < (SIMULATION_HEIGHT / GRADIENT_SCALE) - 1; y++) {
            for (int x = 1; x < (SIMULATION_WIDTH / GRADIENT_SCALE) - 1; x++) {
                planet.mask[y][x].temperature = planet.tempMask[y][x].temperature;
            }
        }
    }
}
