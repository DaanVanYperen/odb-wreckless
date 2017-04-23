package net.mostlyoriginal.game.system.stencil;

import com.badlogic.gdx.Gdx;
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

    @Override
    protected void initialize() {
        super.initialize();
        loadPlanets();
    }

    private void loadPlanets() {
        final Json json = new Json();
        stencilLibrary = json.fromJson(StencilLibrary.class, Gdx.files.internal("stencils.json"));
    }

    public void stencil(String id) {
        final StencilData stencilData = stencilLibrary.getById(id);
        if (stencilData != null) {
            stencil(planetCreationSystem.planetEntity.getPlanet(), new Texture(stencilData.texture), stencilData.replaceTypes);
        }
    }

    Vector2 tv = new Vector2();

    private void stencil(Planet planet, Texture texture, PlanetCell.CellType[] replaceTypes) {
        final TextureData textureData = texture.getTextureData();
        textureData.prepare();

        int degrees = MathUtils.random(360);
        final Pixmap pixmap = textureData.consumePixmap();
        for (int y = 0; y < texture.getHeight(); y++) {
            for (int x = 0; x < texture.getWidth(); x++) {

                tv.set(x,y).rotate(degrees).add(G.SIMULATION_WIDTH / 2, G.SIMULATION_HEIGHT / 2);

                final PlanetCell.CellType type = getSourceCellType(planet, pixmap.getPixel(x, texture.getHeight()-y));
                if (type != null) {
                    replaceCell(planet, Math.round(tv.x),Math.round(tv.y), replaceTypes, type);
                }

            }
        }
        pixmap.dispose();
    }

    private void replaceCell(Planet planet, int tx, int ty, PlanetCell.CellType[] replaceTypes, PlanetCell.CellType type) {
        final PlanetCell cell = planet.get(tx, ty);
        if (cell != null) {
            if (isType(replaceTypes, cell.type)) {
                cell.nextType = type;
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
            if (pixel == type.intColor) {
                return type.type;
            }
        }
        return null;
    }


}
