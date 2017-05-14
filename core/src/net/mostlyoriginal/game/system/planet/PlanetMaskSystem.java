package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;
import net.mostlyoriginal.game.system.common.FluidIntervalIteratingSystem;
import net.mostlyoriginal.game.system.planet.cells.*;

import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetMaskSystem extends FluidIntervalIteratingSystem {

    PlanetSimulationSystem planetSimulationSystem;

    public static final float TEMPERATURE_LOSS = 0.9f;
    private int lace = 0;

    public PlanetMaskSystem() {
        super(Aspect.all(Planet.class), 1 / 30f);
    }

    @Override
    protected void process(E e) {
        lace = (lace + 1) % 2;
        generateMask(e.getPlanet());
    }

    private void generateMask(Planet planet) {
        //clearMask(planet);
        CellDecorator planetCell = new CellDecorator(planet);
        final float delta = world.delta;
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            if (G.INTERLACING_SIMULATION && y % 2 == lace) {
                for (int x = 0; x < SIMULATION_WIDTH; x++) {
                    final PlanetCell cell = planet.grid[y][x];
                    planetSimulationSystem.simulators[cell.type.ordinal()].updateMask(planetCell.proxies(cell), delta);
                }
            }
        }
        smoothMask(planet);
    }

    private void smoothMask(Planet planet) {
        for (int y = 1; y < (SIMULATION_HEIGHT / GRADIENT_SCALE) - 1; y++) {
            if (G.INTERLACING_SIMULATION && y % 2 == lace) {
                for (int x = 1; x < (SIMULATION_WIDTH / GRADIENT_SCALE) - 1; x++) {
                    planet.tempMask[y][x].temperature = planet.mask[y][x].temperature;
                    for (int i = 0; i < 8; i++) {
                        planet.tempMask[y][x].temperature =
                                Math.max(
                                        (planet.mask[y + PlanetCell.directions[i][1]][x + PlanetCell.directions[i][0]].temperature) * TEMPERATURE_LOSS,
                                        planet.tempMask[y][x].temperature
                                );
                    }
                }
            }
        }
        for (int y = 1; y < (SIMULATION_HEIGHT / GRADIENT_SCALE) - 1; y++) {
            for (int x = 1; x < (SIMULATION_WIDTH / GRADIENT_SCALE) - 1; x++) {
                planet.mask[y][x].temperature = MathUtils.clamp(planet.tempMask[y][x].temperature, -100f, StatusMask.MAX_TEMPERATURE) * 0.95f;
            }
        }
    }
}
