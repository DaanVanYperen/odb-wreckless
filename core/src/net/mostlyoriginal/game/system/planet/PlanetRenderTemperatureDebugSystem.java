package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetRenderTemperatureDebugSystem extends FluidIteratingSystem {
    private SpriteBatch batch;
    private TextureRegion planetPixel;

    public PlanetRenderTemperatureDebugSystem() {
        super(Aspect.all(Planet.class));
    }

    public boolean active = false;
    private CameraSystem cameraSystem;

    @Override
    protected void initialize() {
        batch = new SpriteBatch(2000);
        planetPixel = new TextureRegion(new Texture("planetcell.png"),1,1);
    }

    @Override
    protected void begin() {
        super.begin();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        super.end();
        batch.end();
    }

    Color color = new Color();

    @Override
    protected void process(E e) {
        if ( !active ) return;
        Planet planet = e.getPlanet();
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];

                int temperature = planet.getStatusMask(x, y).temperature;
                color.set((temperature > 0 ? temperature * 0.2f : 0), 0, (temperature < 0 ? 1f : 0), Math.abs(temperature) * 0.01f);
                if (temperature != 0) {
                    batch.setColor(color);
                    batch.draw(planetPixel, x+PLANET_X, y+ PLANET_Y);
                }
            }

        }
    }
}