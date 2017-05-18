package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetRenderGravityDebugSystem;
import net.mostlyoriginal.game.system.planet.PlanetRenderTemperatureDebugSystem;
import net.mostlyoriginal.game.system.planet.PlanetSimulationSystem;

import static net.mostlyoriginal.game.component.G.PLANET_X;
import static net.mostlyoriginal.game.component.G.PLANET_Y;

/**
 * @author Daan van Yperen
 */
public class DrawingSystem extends FluidIteratingSystem {

    TagManager tagManager;
    private boolean rightMousePressed;
    private PlanetCell.CellType type = null;

    private PlanetSimulationSystem planetSimulationSystem;
    private PlanetRenderGravityDebugSystem planetRenderGravityDebugSystem;
    private PlanetRenderTemperatureDebugSystem planetRenderTemperatureDebugSystem;
    private boolean middleMousePressed;
    private int size=1;

    public DrawingSystem() {
        super(Aspect.all(Planet.class));
    }

    private boolean leftMousePressed;

    @Override
    protected void begin() {
        super.begin();
        leftMousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        rightMousePressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        middleMousePressed = Gdx.input.isButtonPressed(Input.Buttons.MIDDLE);

        if (G.DEBUG_DRAWING) {
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
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
                type = PlanetCell.CellType.STEAM;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
                type = PlanetCell.CellType.CLOUD;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                planetRenderGravityDebugSystem.active = !planetRenderGravityDebugSystem.active;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                planetRenderTemperatureDebugSystem.active = !planetRenderTemperatureDebugSystem.active;
            }
        }

        final E cursor = E.E(tagManager.getEntityId("cursor"));
        int shade = tagManager.getEntityId("cursorShade");
        if (shade != -1) {
            final E cursorShade = E.E(shade);
            cursorShade.pos(cursor.posX() - 9, cursor.posY() - 9);
        }
    }

    @Override
    protected void process(E e) {
        final E cursor = E.E(tagManager.getEntity("cursor"));
        if (leftMousePressed && type != null) {
            draw(e, (int) cursor.posX(), (int) cursor.posY(), size, type);
        }
        if (rightMousePressed) {
            stopDrawing();
        }
        if (middleMousePressed && G.DEBUG_DRAWING && type != null) {
            draw(e, (int) cursor.posX(), (int) cursor.posY(), 100, type);
        }

    }

    private void stopDrawing() {
        type = null;
        removeDrawingCursor();
    }

    public void startDrawing(PlanetCell.CellType type) {
        stopDrawing();
        if (type != null) {
            E.E()
                    .anim(type.name() + "_cursor")
                    .tag("cursorShade")
                    .renderLayer(G.LAYER_CURSOR);
        }
        this.type = type;
    }

    private void removeDrawingCursor() {
        int shade = tagManager.getEntityId("cursorShade");
        if (shade != -1) {
            E.E(shade).deleteFromWorld();
        }
    }

    Vector2 v = new Vector2();

    public void draw(E e, int x1, int y1, int size, PlanetCell.CellType type) {
        y1 -= PLANET_Y;
        x1 -= PLANET_X;
        for (int y = y1 - size; y < y1 + size; y++) {
            for (int x = x1 - size; x < x1 + size; x++) {
                if (size <= 1 || v.set(x1, y1).sub(x, y).len() < size) {
                    PlanetCell cell = e.getPlanet().get(x, y);
                    if (cell != null) {
                        cell.type = type;
                        if ( type == PlanetCell.CellType.STATIC ) {
                            cell.color = planetSimulationSystem.planet.cellColor[PlanetCell.CellType.STATIC.ordinal()];
                        }
                    }
                }
            }
        }
    }

    public void setSize( int size ) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
