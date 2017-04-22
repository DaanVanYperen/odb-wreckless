package net.mostlyoriginal.game.system.planet;

import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.badlogic.gdx.utils.Align.center;

/**
 * @author Daan van Yperen
 */
public class PlanetCreationSystem extends PassiveSystem {

    GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void initialize() {
        super.initialize();
        Planet planet = E.E()
                .planet()
                .pos(0, 0)
                .getPlanet();
        populate(planet);
        gravity(planet);
    }

    private void gravity(Planet planet) {
        int centerX = Planet.SIMULATION_WIDTH / 2;
        int centerY = Planet.SIMULATION_HEIGHT / 2;
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
        int x1 = Planet.SIMULATION_WIDTH / 2;
        int y1 = Planet.SIMULATION_HEIGHT / 2;
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

    private void populate(Planet planet) {
        formSurface(planet);
        formMask(planet);
    }

    private void formSurface(Planet planet) {
        TextureData textureData = ((TextureRegion) gameScreenAssetSystem.get("dancingman").getKeyFrame(0)).getTexture().getTextureData();
        textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();
        for (int y = 0; y < Planet.SIMULATION_HEIGHT; y++) {
            for (int x = 0; x < Planet.SIMULATION_WIDTH; x++) {
                planet.grid[y][x] = formCell(planet, y, x, pixmap.getPixel(x, y));
            }
        }
        pixmap.dispose();
    }

    private void formMask(Planet planet) {
        for (int y = 0; y < Planet.SIMULATION_HEIGHT / Planet.GRADIENT_SCALE; y++) {
            for (int x = 0; x < Planet.SIMULATION_WIDTH / Planet.GRADIENT_SCALE; x++) {
                planet.mask[y][x] = new StatusMask();
                planet.tempMask[y][x] = new StatusMask();
            }
        }
    }

    Color c = new Color();

    private PlanetCell formCell(Planet planet, int y, int x, int color) {

        PlanetCell cell = new PlanetCell();
        cell.x = x;
        cell.y = y;
        cell.color = color;
        c.set(color);

        guessCellType(cell);

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

    private void guessCellType(PlanetCell cell) {
        if (c.r > 0.38f &&c.b < 0.25f) {
            cell.type = PlanetCell.CellType.LAVA;
        } else if (c.r < 0.03f && c.g > 0.40f && c.g < 0.50f && c.b > 0.5f) {
            cell.type = PlanetCell.CellType.WATER;
        } else if (c.r < 0.2f && c.g > 0.50f && c.g < 0.70f && c.b > 0.5f) {
            cell.type = PlanetCell.CellType.AIR;
        } else if (c.r > 0.7f && c.g > 0.7f && c.b > 0.8f) {
            cell.type = PlanetCell.CellType.ICE;
        }
    }
}
