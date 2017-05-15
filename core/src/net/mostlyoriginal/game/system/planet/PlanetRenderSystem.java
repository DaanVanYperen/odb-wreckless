package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.utils.reflect.ClassReflection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidDeferredEntityProcessingSystem;

import javax.sql.rowset.serial.SerialJavaObject;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGB888;
import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetRenderSystem extends FluidDeferredEntityProcessingSystem {

    private SpriteBatch batch;
    private TextureRegion planetPixel;
    private Pixmap pixmap;
    private Texture pixmapAsTexture;
    private byte[] workingCopy;
    private int[] workingCopyInt;
    private ByteBuffer buffer;

    public PlanetRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Planet.class), principal);
    }

    private CameraSystem cameraSystem;

    @Override
    protected void initialize() {
        batch = new SpriteBatch(2000);

        planetPixel = new TextureRegion(new Texture("planetcell.png"), 1, 1);

        pixmap = new Pixmap(SIMULATION_WIDTH, SIMULATION_HEIGHT, RGBA8888);
        pixmapAsTexture = new Texture(SIMULATION_WIDTH, SIMULATION_HEIGHT, RGBA8888);
        pixmapAsTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        buffer = ByteBuffer.allocate(4 * SIMULATION_WIDTH * SIMULATION_HEIGHT);
//        byteBufferDirect = ByteBuffer.allocateDirect(4 * SIMULATION_WIDTH * SIMULATION_HEIGHT);
    }

    @Override
    protected void begin() {
        super.begin();
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
        super.end();
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

        buffer.rewind();
        for (int y = 0; y < SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < SIMULATION_WIDTH; x++) {
                buffer.putInt(planet.grid[y][x].color);
            }
        }
        buffer.rewind();
        MyScreenUtils.putPixelsBack(pixmap, buffer);
        pixmapAsTexture.draw(pixmap, 0, 0);

        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(pixmapAsTexture, PLANET_X, PLANET_Y + G.SIMULATION_HEIGHT, G.SIMULATION_WIDTH, -G.SIMULATION_HEIGHT);
    }

    private void renderClouds(Planet planet) {
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
    }
}