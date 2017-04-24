package net.mostlyoriginal.game.system.stencil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.system.planet.FauxPixMap;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
import net.mostlyoriginal.game.component.PlanetData;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class PlanetStencilSystem extends PassiveSystem {

    GameScreenAssetSystem gameScreenAssetSystem;
    PlanetCreationSystem planetCreationSystem;

    StencilLibrary stencilLibrary = new StencilLibrary();
    private Color c = new Color();

    @Override
    protected void initialize() {
        super.initialize();
        loadPlanets();
    }

    private void loadPlanets() {
        final Json json = new Json();
        stencilLibrary = json.fromJson(StencilLibrary.class, Gdx.files.internal("stencils.json"));
    }

    public void stencilRotateCenter(String id) {
        final StencilData stencilData = stencilLibrary.getById(id);
        if (stencilData != null) {
            stencil(planetCreationSystem.planetEntity.getPlanet(),
                    new Texture(stencilData.texture),
                    stencilData.replaceTypes,
                    MathUtils.random(360),
                    G.SIMULATION_WIDTH / 2, G.SIMULATION_HEIGHT / 2);
        }
    }

    public void stencil(String id) {
        final StencilData stencilData = stencilLibrary.getById(id);
        if (stencilData != null) {
            Texture texture = new Texture(stencilData.texture);
            stencil(planetCreationSystem.planetEntity.getPlanet(), texture,
                    stencilData.replaceTypes, 0,
                    (G.SIMULATION_WIDTH / 2) - texture.getWidth() / 2, (G.SIMULATION_HEIGHT / 2) - texture.getHeight() / 2);
        }
    }

    Vector2 tv = new Vector2();

    private void stencil(Planet planet, Texture texture, PlanetCell.CellType[] replaceTypes, int degrees, int x1, int y1) {
        final TextureData textureData = texture.getTextureData();
        textureData.prepare();

        final Pixmap pixmap = textureData.consumePixmap();
        for (int y = 0; y < texture.getHeight(); y++) {
            for (int x = 0; x < texture.getWidth(); x++) {

                tv.set(x, y).rotate(degrees).add(x1, y1);

                int color = pixmap.getPixel(x, texture.getHeight() - y);
                final PlanetCell.CellType type = getSourceCellType(planet, color);
                if (type != null) {
                    replaceCell(planet, Math.round(tv.x), Math.round(tv.y), replaceTypes, type, color);
                }

            }
        }
        pixmap.dispose();
    }

    private void replaceCell(Planet planet, int tx, int ty, PlanetCell.CellType[] replaceTypes, PlanetCell.CellType type, int color) {
        final PlanetCell cell = planet.get(tx, ty);
        if (cell != null) {
            if (isType(replaceTypes, cell.type)) {
                cell.nextType = type;
                cell.nextColor = color;
            }
        }
    }

    private boolean isType(PlanetCell.CellType[] replaceTypes, PlanetCell.CellType type) {
        for (PlanetCell.CellType replaceType : replaceTypes) {
            if (replaceType == type) return true;
        }
        return false;
    }

    private PlanetCell.CellType getSourceCellType(Planet planet, int pixel) {
        for (PlanetData.CellType type : planet.data.types) {
            if (FauxPixMap.sameIsh(pixel, type.intColor)) {
                return type.type;
            }
        }
        Color.rgba8888ToColor(c, pixel);
        if (c.a != 0) {
            return PlanetCell.CellType.STATIC;
        }

        return null;
    }


}
