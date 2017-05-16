package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidDeferredEntityProcessingSystem;

import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetRenderSystem extends FluidDeferredEntityProcessingSystem {

    private SpriteBatch batch;
    private TextureRegion planetPixel;
    private FrameBuffer frameBuffer;
    private int[] dirtyMask;
    private OrthographicCamera vboCamera;

    public PlanetRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Planet.class), principal);
    }

    private CameraSystem cameraSystem;

    @Override
    protected void initialize() {
        batch = new SpriteBatch(4000);
        new ShapeRenderer();

        planetPixel = new TextureRegion(new Texture("planetcell.png"), 1, 1);

        dirtyMask = new int[SIMULATION_WIDTH * SIMULATION_HEIGHT];

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, SIMULATION_WIDTH, SIMULATION_HEIGHT, false);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);
        frameBuffer.begin();
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        frameBuffer.end();

        vboCamera = new OrthographicCamera(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        vboCamera.setToOrtho(true, SIMULATION_WIDTH, SIMULATION_HEIGHT);
        vboCamera.update();
    }

    private Color color = new Color();

    @Override
    protected void process(E e) {
        Planet planet = e.getPlanet();
        renderMain(planet);
        renderClouds(planet);
    }

    private int lastColor = 0;


    private void renderMain(Planet planet) {
        lastColor = -1;

        frameBuffer.begin();


        batch.setProjectionMatrix(vboCamera.combined);
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        batch.begin();
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
               if (dirtyMask[x + y * SIMULATION_WIDTH] != cell.color) {
                    dirtyMask[x + y * SIMULATION_WIDTH] = cell.color;
                        Color.rgba8888ToColor(color, cell.color);
                    batch.setColor(color);
                    batch.draw(planetPixel, x, y, 1, 1);
                }
            }
        }
        batch.end();
        frameBuffer.end();

        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(frameBuffer.getColorBufferTexture(), PLANET_X, PLANET_Y, G.SIMULATION_WIDTH, G.SIMULATION_HEIGHT);
        batch.end();
    }

    private void renderClouds(Planet planet) {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
        Color.rgba8888ToColor(color, lastColor);
        batch.setColor(lastColor);
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                if (cell.type == PlanetCell.CellType.CLOUD) {
                    if (cell.color != lastColor) {
                        lastColor = cell.color;
                        Color.rgba8888ToColor(color, lastColor);
                        batch.setColor(color);
                    }
                    batch.draw(planetPixel, x + PLANET_X - 1, y + PLANET_Y - 1, 3, 3);
                }
            }
        }
        batch.end();
    }
}