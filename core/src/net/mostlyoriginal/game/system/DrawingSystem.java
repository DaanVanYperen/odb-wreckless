package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.dilemma.CardScriptSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
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
    private int size = 1;
    private EntityType entityType;
    private CardScriptSystem cardScriptSystem;
    private PlanetCreationSystem planetCreationSystem;
    private int lastY;
    private int lastX;

    public DrawingSystem() {
        super(Aspect.all(Planet.class));
    }

    private boolean leftMousePressed;
    private boolean clickedOnce;
    private int down = 0;

    @Override
    protected void begin() {
        super.begin();
        leftMousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        if (leftMousePressed) {
            down++;
            clickedOnce = down++ == 1;
        } else {
            clickedOnce = false;
            down = 0;
        }

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
    public void drawLine(E e, int ox, int oy, int x2, int y2, int size, PlanetCell.CellType type) {
        int x = ox;
        int y = oy;
        int w = x2 - x;
        int h = y2 - y;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w < 0) dx1 = -1;
        else if (w > 0) dx1 = 1;
        if (h < 0) dy1 = -1;
        else if (h > 0) dy1 = 1;
        if (w < 0) dx2 = -1;
        else if (w > 0) dx2 = 1;
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) dy2 = -1;
            else if (h > 0) dy2 = 1;
            dx2 = 0;
        }
        int numerator = longest >> 1;
        int lastX = x, lastY = y;
        for (int i = 0; i <= longest; i++) {
            draw(e, x, y,size,type);
            lastX = x;
            lastY = y;
            numerator += shortest;
            if (!(numerator < longest)) {
                numerator -= longest;
                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }
        }
    }

    @Override
    protected void process(E e) {
        final E cursor = E.E(tagManager.getEntity("cursor"));
        if (leftMousePressed && type != null && cursor.posY() > G.TOOL_HEIGHT) {
            if ( lastX != -1 ) {
                drawLine(e, (int) cursor.posX(), (int) cursor.posY(), lastX, lastY, size, type);
            } else {
                draw(e, (int) cursor.posX(), (int) cursor.posY(), size, type);
            }
            lastX=(int)cursor.posX();
            lastY=(int)cursor.posY();
        } else {
            lastX = lastY = -1;
        }
        if (clickedOnce && entityType != null&& cursor.posY() > G.TOOL_HEIGHT) {
            draw((int) cursor.posX(), (int) cursor.posY(), entityType);
        }
        if (rightMousePressed) {
            stopDrawing();
        }
        if (middleMousePressed && G.DEBUG_DRAWING && type != null) {
            draw(e, (int) cursor.posX(), (int) cursor.posY(), 100, type);
        }

    }

    private void draw(int x, int y, EntityType entityType) {
        switch (entityType) {
            case ALIEN:
                planetCreationSystem.spawnDude(x, y).massInverse(true).alien();
                break;
            case ICBM:
                cardScriptSystem.spawnStructure("icbm", G.LAYER_STRUCTURES_FOREGROUND)
                        .explosiveYield(MathUtils.random(5,10)).pos(x, y);
                break;
            case SKYSCRAPER:
                cardScriptSystem.spawnStructure("skyscraper" + MathUtils.random(7), G.LAYER_STRUCTURES_BACKGROUND).pos(x, y);
                break;
            case DOLPHIN:
                E e = planetCreationSystem.spawnDude(x, y);
                Vector2 gravityVector = v.set(e.planetboundGravity()).rotate(180f);
                e
                        .dolphinized()
                        .physicsVr(1000f)
                        .physicsVx(gravityVector.x * 100f)
                        .physicsVy(gravityVector.y * 100f);
                break;
            case DUDE:
                planetCreationSystem.spawnDude(x, y).orientToGravityIgnoreFloor(false);
                break;
        }
    }

    private void stopDrawing() {
        entityType = null;
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
                        if (type == PlanetCell.CellType.STATIC) {
                            cell.color = planetSimulationSystem.planet.dirtColor[y][x];
                        }
                    }
                }
            }
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void startDrawing(EntityType entityType) {
        stopDrawing();
        if (entityType != null) {
            E.E()
                    .anim(entityType.name() + "_cursor")
                    .tag("cursorShade")
                    .renderLayer(G.LAYER_CURSOR);
        }
        this.entityType = entityType;
    }
}
