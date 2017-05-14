package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;
import net.mostlyoriginal.game.system.common.FluidIntervalIteratingSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.cells.*;

import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetSimulationSystem extends FluidIntervalIteratingSystem {

    public static final int AIR_ORDINAL = PlanetCell.CellType.AIR.ordinal();
    public static final int NOTHING_ORDINAL = PlanetCell.CellType.NOTHING.ordinal();
    public CellSimulator[] simulators = new CellSimulator[PlanetCell.CellType.values().length];
    public int[] simulatedBlocks = new int[PlanetCell.CellType.values().length];
    private Vector2 v = new Vector2();
    private int lace = 0;
    private PlanetCreationSystem planetCreationSystem;

    public PlanetSimulationSystem() {
        super(Aspect.all(Planet.class), 1 / 30f);
    }

    /**
     * Don't count air in simualted blocks.
     */
    public int totalSimulatedBlocks() {
        int count = 0;
        for (int i = 0, s = PlanetCell.CellType.values().length; i < s; i++) {
            if (i == AIR_ORDINAL || i == NOTHING_ORDINAL)
                continue;
            count += simulatedBlocks[i];
        }
        return count;
    }

    @Override
    protected void initialize() {
        super.initialize();

        addSimulator(PlanetCell.CellType.STATIC, new StaticCellSimulator());
        addSimulator(PlanetCell.CellType.LAVA, new LavaCellSimulator());
        addSimulator(PlanetCell.CellType.LAVA_CRUST, new LavaCellSimulator());
        addSimulator(PlanetCell.CellType.WATER, new WaterCellSimulator());
        addSimulator(PlanetCell.CellType.AIR, new AirCellSimulator());
        addSimulator(PlanetCell.CellType.ICE, new IceCellSimulator());
        addSimulator(PlanetCell.CellType.STEAM, new SteamCellSimulator());
        addSimulator(PlanetCell.CellType.CLOUD, new CloudCellSimulator());
        addSimulator(PlanetCell.CellType.FIRE, new FireCellSimulator());
        addSimulator(PlanetCell.CellType.NOTHING, new NothingCellSimulator());

        process(planetCreationSystem.planetEntity);
    }

    private void addSimulator(PlanetCell.CellType cellType, CellSimulator simulator) {
        simulators[cellType.ordinal()] = simulator;
        simulatedBlocks[cellType.ordinal()] = 0;
    }

    @Override
    protected void process(E e) {
        final Planet planet = e.getPlanet();
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
                        (int) MathUtils.clamp((h / 4f) - (v.set(w / 2, h / 2).sub(w / 4 + x / 2, y).len()), -200, 200) / 5;
            }
        }
    }

    private void activateChanges(Planet planet) {
        CellDecorator planetCell = new CellDecorator(planet);
        final float delta = world.delta;
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                if (!DEBUG_NO_SECOND_PASS) cell.activateNextType();
                simulators[cell.type.ordinal()].color(planetCell.proxies(cell), delta);
            }
        }
    }

    private void simulateCells(Planet planet) {
        CellDecorator planetCell = new CellDecorator(planet);
        final float delta = world.delta;
        int IGNORE_BAND_X = 0;
        int IGNORE_BAND_Y = 0;
        clearSimulationCounts();
        lace = (lace + 1) % 2;

        for (int y = IGNORE_BAND_Y; y < SIMULATION_HEIGHT - +IGNORE_BAND_Y; y++) {
            if (G.INTERLACING_SIMULATION && y % 2 == lace) {

                for (int x = IGNORE_BAND_X; x < SIMULATION_WIDTH - +IGNORE_BAND_X; x++) {
                    final PlanetCell cell = planet.grid[y][x];
                    simulatedBlocks[cell.type.ordinal()]++;
                }
                continue;
            }
            for (int x = IGNORE_BAND_X; x < SIMULATION_WIDTH - +IGNORE_BAND_X; x++) {
                final PlanetCell cell = planet.grid[y][x];
                final int ordinal = cell.type.ordinal();
                simulatedBlocks[ordinal]++;
                if (cell.sleep > 0) {
                    cell.sleep--;
                    continue;
                }
                simulators[ordinal].process(planetCell.proxies(cell), delta);
                if (DEBUG_NO_SECOND_PASS) cell.activateNextType();
            }
        }
    }

    private void clearSimulationCounts() {
        for (int i = 0; i < PlanetCell.CellType.values().length; i++) {
            simulatedBlocks[i] = 0;
        }
    }
}
