package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static net.mostlyoriginal.game.component.G.PLANET_X;
import static net.mostlyoriginal.game.component.G.PLANET_Y;

/**
 * @author Daan van Yperen
 */
public class PlanetRenderGravityDebugSystem extends FluidIteratingSystem {
    private SpriteBatch batch;
    private TextureRegion planetPixel;
    public boolean active;

    public PlanetRenderGravityDebugSystem() {
        super(Aspect.all(Planet.class));
    }

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
    int direction = 0;
    float cooldown = 0.5f;

    @Override
    protected void process(E e) {
        if ( !active ) return;
        Planet planet = e.getPlanet();
        cooldown -= world.delta;
        if (cooldown <= 0) {
            cooldown = 0.5f;
            direction = (direction + 1) % 8;
//            System.out.println(PlanetCell.directions[direction][0] + ", " + PlanetCell.directions[direction][1]);
        }
        for (int y = 0; y < G.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < G.SIMULATION_WIDTH; x++) {
                final PlanetCell cell = planet.grid[y][x];
                if (cell.down == direction) {
                    color.set(1f, 0, 0f, 0.4f);
                    batch.setColor(color);
                    batch.draw(planetPixel, x+PLANET_X, y+ PLANET_Y);
                }
            }

        }
    }
}