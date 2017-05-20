package net.mostlyoriginal.game.system.planet;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.DrawingSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;
import static net.mostlyoriginal.game.component.G.*;

/**
 * @author Daan van Yperen
 */
public class PlanetCreationSystem extends PassiveSystem {

    GameScreenAssetSystem gameScreenAssetSystem;
    PlanetLibrary planetLibrary = new PlanetLibrary();
    public E planetEntity;

    Pos logoStartPos = new Pos((G.SCREEN_WIDTH / CAMERA_ZOOM) / 2 - G.LOGO_WIDTH / 2, (G.SCREEN_HEIGHT) / 2);
    Pos logoEndPos = new Pos((G.SCREEN_WIDTH / CAMERA_ZOOM) / 2 - G.LOGO_WIDTH / 2, (G.SCREEN_HEIGHT) / 2 + G.LOGO_HEIGHT);
    private DrawingSystem drawingSystem;
    private TagManager tagManager;

    @Override
    protected void initialize() {
        super.initialize();
        loadPlanets();

        if (!G.DEBUG_SKIP_INTRO) {
            E.E()
                    .pos()
                    .anim("logo")
                    .animLoop(false)
                    .renderLayer(10000)
                    .tint(Tint.TRANSPARENT)
                    .script(
                            parallel(
                                    tween(logoStartPos, logoEndPos, 10f, Interpolation.linear),
                                    sequence(
                                            delay(2f),
                                            tween(Tint.TRANSPARENT, Tint.WHITE, 1f, Interpolation.linear),
                                            delay(2f),
                                            tween(Tint.WHITE, Tint.TRANSPARENT, 4f, Interpolation.linear)
                                    )
                            )
                    );
        }
    }

    private void loadPlanets() {
        final Json json = new Json();
        planetLibrary = json.fromJson(PlanetLibrary.class, Gdx.files.internal("planets.json"));

        net.mostlyoriginal.game.component.PlanetData planet = planetLibrary.planets[1];
        {

            planetEntity = E.E();
            Planet planetE = planetEntity
                    .renderLayer(G.LAYER_PLANET)
                    .planet()
                    .tag("planet")
                    .pos(0, 0)
                    .getPlanet();

            planetE.data = planet;

            for (net.mostlyoriginal.game.component.PlanetData.CellType type : planet.types) {
                Color color = Color.valueOf(type.color);
                Color colorArid = type.colorSecondary != null ? Color.valueOf(type.colorSecondary) : color;
                planetE.cellColor[type.type.ordinal()] = type.intColor = Color.rgba8888(color);
                planetE.cellColorSecondary[type.type.ordinal()] = type.intColorSecondary = Color.rgba8888(colorArid);
            }

            populate(planet, planetE);
            gravity(planetE);
            height(planetE);
        }
    }

    private void height(Planet planetE) {
        for (int y = 0; y < G.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < G.SIMULATION_WIDTH; x++) {
                planetE.grid[y][x].height = (v.set(x, y).sub(G.SIMULATION_WIDTH / 2f, G.SIMULATION_HEIGHT / 2f).len());
            }
        }
    }

    private void gravity(Planet planet) {
        int centerX = SIMULATION_WIDTH / 2;
        int centerY = SIMULATION_HEIGHT / 2;
        int granularity = 30;
        if (DEBUG_NAIVE_GRAVITY) {
            for (int y = 0; y < G.SIMULATION_HEIGHT; y++) {
                for (int x = 0; x < G.SIMULATION_WIDTH; x++) {
                    PlanetCell planetCell = planet.get(x, y);
                    if (planetCell != null) {
                        if (planetCell.down == -1) {
                            Vector2 nor = v.set(centerX, centerY).sub(planetCell.x, planetCell.y).nor();
                            planetCell.down = (dirOf(Math.round(nor.x), Math.round(nor.y)));
                        }
                    }
                }
            }
        } else {
            for (int sub = 0; sub < granularity; sub++) {
                for (int degrees = 0; degrees < 360; degrees++) {
                    Vector2 source = v.set(240, 0).rotate(degrees + sub * (1f / granularity)).add(centerX, centerY);
                    drawGravity(planet, Math.round(source.x), Math.round(source.y), centerX, centerY);
                }
            }
        }

        for (int degrees = 0; degrees < 360; degrees += 15) {
            if (MathUtils.random(1, 100) < 25) {
                spawnCloud(degrees);
            }
            spawnDude(degrees);
        }
    }

    private void spawnCloud(int degrees) {
        Vector2 source = v.set(125, 0).rotate(degrees).add(SIMULATION_WIDTH / 2, SIMULATION_HEIGHT / 2);
        drawingSystem.draw(planetEntity, (int) source.x + PLANET_X, (int) source.y + PLANET_Y, MathUtils.random(1, 3), PlanetCell.CellType.CLOUD);
    }

    public void spawnDude(int degrees) {
        Vector2 source = getSpawnLocation(degrees);
        spawnDude(source.x, source.y);
    }

    public Vector2 getSpawnLocation() {
        return getSpawnLocation(MathUtils.random(0, 359));
    }

    public Vector2 getSpawnLocationHollowEarth() {
        return getSpawnLocationHollowEarth(MathUtils.random(0, 359));
    }

    public Vector2 getSpawnLocation(int degrees) {
        return v.set(130, 0).rotate(degrees).add(SIMULATION_WIDTH / 2, SIMULATION_HEIGHT / 2).add(PLANET_X, PLANET_Y);
    }

    public Vector2 getSpawnLocationHollowEarth(int degrees) {
        return v.set(40, 0).rotate(degrees).add(SIMULATION_WIDTH / 2, SIMULATION_HEIGHT / 2).add(PLANET_X, PLANET_Y);
    }

    public E spawnDude(float x, float y) {
        E dude = E.E()
                .anim("dude")
                .renderLayer(G.LAYER_DUDES)
                .pos(x, y)
                .angle()
                .originX(0.5f)
                .originY(0.1f)
                .physics()
                .planetbound()
                .flammable()
                .wander()
                .mass()
                .orientToGravity();
        if (DEBUG_NO_ENTITY_RENDERING) {
            dude.invisible();
        }
        return dude;
    }

    public void drawGravity(Planet planet, int ox, int oy, int x2, int y2) {
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
            plotGravity(planet, x, y, lastX, lastY);
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

    private void plotGravity(Planet planet, int x0, int y0, int lastX, int lastY) {

        PlanetCell planetCell = planet.get(x0, y0);
        if (planetCell != null) {
            if (planetCell.down == -1) {
                planetCell.down = (dirOf(x0 - lastX, y0 - lastY));
            }
        }

    }

    private int dirOfSmart(int x, int y) {
        int x0 = x;
        int y0 = y;
        int x1 = G.SIMULATION_WIDTH / 2;
        int y1 = G.SIMULATION_HEIGHT / 2;
        int Dx = x0 - x1;
        int Dy = y0 - y1;

        int fraction = 0, xstep, ystep;

        if (Dy < 0) {
            Dy = -Dy;
            ystep = -1;
        } else {
            ystep = 1;
        }
        if (Dx < 0) {
            Dx = -Dx;
            xstep = -1;
        } else {
            xstep = 1;
        }

        Dy <<= 1;
        Dx <<= 1;

        int array[] = {250, 0, 0, 250};
        if (Dx > Dy) {
            fraction = Dy - (Dx >> 1);
        }
        while (x0 != x1) {
            if (fraction >= 0) {
                y0 += ystep;
                fraction -= Dx;
                x0 += xstep;
                fraction += Dy;
                return dirOf(x - x0, y - y0);
            } else {
                fraction = Dx - (Dy >> 1);
                while (y0 != y1) {
                    if (fraction >= 0) {
                        x0 += xstep;
                        fraction -= Dy;
                    }
                    y0 += ystep;
                    fraction += Dx;
                    return dirOf(x - x0, y - y0);
                }
            }
        }

        return 0;
    }

    private void populate(net.mostlyoriginal.game.component.PlanetData planetData, Planet planet) {
        formSurface(planet, new FauxPixMap(planetData.texture), planetData);
        buildDirtColorMask(planet, new FauxPixMap(planetData.dirtTexture));
        formMask(planet);
    }

    private void buildDirtColorMask(Planet planet, FauxPixMap fauxPixMap) {
        for (int y = 0; y < G.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < G.SIMULATION_WIDTH; x++) {
                planet.dirtColor[y][x] = fauxPixMap.getPixel(x, y);
            }
        }
    }

    private void formSurface(Planet planet, FauxPixMap pixmap, PlanetData planetData) {
        for (int y = 0; y < G.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < G.SIMULATION_WIDTH; x++) {
                planet.grid[y][x] = formCell(planet, y, x, pixmap.getPixel(x, y), planetData);
            }
        }
        pixmap.dispose();
    }

    private void formMask(Planet planet) {
        for (int y = 0; y < G.SIMULATION_HEIGHT / G.GRADIENT_SCALE; y++) {
            for (int x = 0; x < G.SIMULATION_WIDTH / G.GRADIENT_SCALE; x++) {
                planet.mask[y][x] = new StatusMask();
                planet.tempMask[y][x] = new StatusMask();
            }
        }
    }

    Color c = new Color();

    private PlanetCell formCell(Planet planet, int y, int x, int color, net.mostlyoriginal.game.component.PlanetData planetData) {

        PlanetCell cell = new PlanetCell();
        cell.x = x;
        cell.y = y;
        cell.color = color;
        c.set(color);
        guessCellType(cell, planetData);

        return cell;
    }

    Vector2 v = new Vector2();

    private int dirOf(int x, int y) {
        for (int i = 0; i < 8; i++) {
            if (PlanetCell.directions[i][0] == x && PlanetCell.directions[i][1] == y) {
                return i;
            }
        }
        return 0;
    }


    private void guessCellType(PlanetCell cell, net.mostlyoriginal.game.component.PlanetData planetData) {

        Color.rgba8888ToColor(c, cell.color);
        if (c.a == 0) {
            cell.type = PlanetCell.CellType.NOTHING;
            return;
        }

        for (net.mostlyoriginal.game.component.PlanetData.CellType type : planetData.types) {
            if (FauxPixMap.sameIsh(cell.color, type.intColor)) {
                cell.type = type.type != PlanetCell.CellType.AIR && DEBUG_AIR_PLANET ? PlanetCell.CellType.AIR : type.type;
                return;
            }
        }

        if (DEBUG_AIR_PLANET) {
            cell.type = PlanetCell.CellType.AIR;
        }
    }

    public void restart() {
        deleteInhabitants();
        deletePlanets();
        loadPlanets();
    }

    private void deleteInhabitants() {
        IntBag entities = world.getAspectSubscriptionManager().get(Aspect.all(Planetbound.class)).getEntities();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            e.deleteFromWorld();
        }
    }

    private void deletePlanets() {

        E.E(tagManager.getEntityId("planet")).deleteFromWorld();
    }
}
