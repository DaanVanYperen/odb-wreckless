package net.mostlyoriginal.game.system.planet;

import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.game.component.G.SIMULATION_HEIGHT;
import static net.mostlyoriginal.game.component.G.SIMULATION_WIDTH;

/**
 * @author Daan van Yperen
 */
public class PlanetCreationSystem extends PassiveSystem {

    GameScreenAssetSystem gameScreenAssetSystem;
    PlanetLibrary planetLibrary = new PlanetLibrary();
    public E planetEntity;

    @Override
    protected void initialize() {
        super.initialize();
        loadPlanets();
    }

    private void loadPlanets() {
        final Json json = new Json();
        planetLibrary = json.fromJson(PlanetLibrary.class, Gdx.files.internal("planets.json"));

        for (net.mostlyoriginal.game.component.PlanetData planet : planetLibrary.planets) {

            planetEntity = E.E();
            Planet planetE = planetEntity
                    .planet()
                    .pos(0, 0)
                    .getPlanet();

            planetE.data = planet;

            for (net.mostlyoriginal.game.component.PlanetData.CellType type : planet.types) {
                Color color = Color.valueOf(type.color);
                Color colorArid = type.colorArid != null ? Color.valueOf(type.colorArid) : color;
                planetE.cellColor[type.type.ordinal()] = type.intColor = Color.rgba8888(color);
                planetE.cellColorArid[type.type.ordinal()] = type.intColorArid = Color.rgba8888(colorArid);
            }

            populate(planet, planetE);
            gravity(planetE);
            height(planetE);
        }
    }

    private void height(Planet planetE) {
        for (int y = 0; y < G.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < G.SIMULATION_WIDTH; x++) {
                planetE.grid[y][x].height = (v.set(x,y).sub(G.SIMULATION_WIDTH/2f, G.SIMULATION_HEIGHT/2f).len());
            }
        }
    }

    private void gravity(Planet planet) {
        int centerX = SIMULATION_WIDTH / 2;
        int centerY = SIMULATION_HEIGHT / 2;
        int granularity = 30;
        for (int sub = 0; sub < granularity; sub++) {
            for (int degrees = 0; degrees < 360; degrees++) {
                Vector2 source = v.set(240, 0).rotate(degrees + sub * (1f / granularity)).add(centerX, centerY);
                drawGravity(planet, Math.round(source.x), Math.round(source.y), centerX, centerY);
            }
        }
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
        formSurface(planet, new Texture(planetData.texture), planetData);
        formMask(planet);
    }

    private void formSurface(Planet planet, Texture texture, net.mostlyoriginal.game.component.PlanetData planetData) {
        TextureData textureData = texture.getTextureData();
        textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();
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
        for (net.mostlyoriginal.game.component.PlanetData.CellType type : planetData.types) {
            if (cell.color == type.intColor) {
                cell.type = type.type;
                return;
            }
        }
    }
}
