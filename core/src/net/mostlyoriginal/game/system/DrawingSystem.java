package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetRenderGravityDebugSystem;
import net.mostlyoriginal.game.system.planet.PlanetRenderTemperatureDebugSystem;

/**
 * @author Daan van Yperen
 */
public class DrawingSystem extends FluidIteratingSystem {

    TagManager tagManager;
    private boolean rightMousePressed;
    private PlanetCell.CellType type = PlanetCell.CellType.AIR;

    private PlanetRenderGravityDebugSystem planetRenderGravityDebugSystem;
    private PlanetRenderTemperatureDebugSystem planetRenderTemperatureDebugSystem;

    public DrawingSystem() {
        super(Aspect.all(Planet.class));
    }

    private boolean leftMousePressed;

    @Override
    protected void begin() {
        super.begin();
        leftMousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        rightMousePressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            type = PlanetCell.CellType.AIR;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            type = PlanetCell.CellType.WATER;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            type = PlanetCell.CellType.LAVA;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            type = PlanetCell.CellType.ICE;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            planetRenderGravityDebugSystem.active = !planetRenderGravityDebugSystem.active;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            planetRenderTemperatureDebugSystem.active = !planetRenderTemperatureDebugSystem.active;
        }
    }

    @Override
    protected void process(E e) {
        final E cursor = E.E(tagManager.getEntity("cursor"));
        if (leftMousePressed) {
            draw(e, (int) cursor.posX(), (int) cursor.posY(), 20, type);
        }
        if (rightMousePressed) {
            draw(e, (int) cursor.posX(), (int) cursor.posY(), 3, type);
        }

    }

    Vector2 v = new Vector2();

    private void draw(E e, int x1, int y1, int size, PlanetCell.CellType air) {
        for (int y = y1 - size; y < y1 + size; y++) {
            for (int x = x1 - size; x < x1 + size; x++) {
                if (v.set(x1, y1).sub(x, y).len() < size) {
                    PlanetCell cell = e.getPlanet().get(x, y);
                    if (cell != null) {
                        cell.type = air;
                    }
                }
            }
        }
    }
}
