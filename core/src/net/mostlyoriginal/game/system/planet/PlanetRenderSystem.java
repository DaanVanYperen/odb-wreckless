package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class PlanetRenderSystem extends FluidIteratingSystem {

    private SpriteBatch batch;
    private Texture planetPixel;

    public PlanetRenderSystem() {
        super(Aspect.all(Planet.class));
    }

    private CameraSystem cameraSystem;

    @Override
    protected void initialize() {
        batch = new SpriteBatch(2000);

        planetPixel = new Texture("planetcell.png");
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
        Planet planet = e.getPlanet();
        for (int y = 0; y < Planet.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < Planet.SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                final Color c = color.set(cell.color);
                batch.setColor(c);
                batch.draw(planetPixel, x, y);
            }
        }
    }
}
