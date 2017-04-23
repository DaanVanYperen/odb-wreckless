package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidDeferredEntityProcessingSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetRenderSystem extends FluidDeferredEntityProcessingSystem {

    private SpriteBatch batch;
    private TextureRegion planetPixel;
    private TextureRegion cloudPixel;

    public PlanetRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Planet.class), principal);
    }

    private CameraSystem cameraSystem;

    @Override
    protected void initialize() {
        batch = new SpriteBatch(2000);

        planetPixel = new TextureRegion(new Texture("planetcell.png"), 1, 1);
        cloudPixel = new TextureRegion(new Texture("planetcell.png"), 11, 17, 17, 13);
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

    private Color color = new Color();

    @Override
    protected void process(E e) {
        Planet planet = e.getPlanet();
        renderMain(planet);
        renderClouds(planet);
    }

    private void renderMain(Planet planet) {
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                if (cell.color == 0)
                    continue;
                if (cell.type != PlanetCell.CellType.CLOUD) {
                    Color.rgba8888ToColor(color, cell.color);
                    batch.setColor(color);
                    batch.draw(planetPixel, x + PLANET_X, y + PLANET_Y);
                }
            }

        }
    }

    private void renderClouds(Planet planet) {
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                if (cell.type == PlanetCell.CellType.CLOUD) {
                    int rndSize = 3;
                    Color.rgba8888ToColor(color, cell.color);
                    batch.setColor(color);
                    batch.draw(planetPixel, x + PLANET_X - ((rndSize - 1) / 2), y + PLANET_Y - ((rndSize - 1) / 2), rndSize, rndSize);
                }
            }

        }
    }
}