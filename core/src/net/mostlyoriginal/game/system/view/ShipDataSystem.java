package net.mostlyoriginal.game.system.view;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.ShipData;
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.render.ShipLibrary;
import net.mostlyoriginal.game.system.render.SpriteLibrary;

import static net.mostlyoriginal.game.component.G.DEBUG_NO_MUSIC;

/**
 * @author Daan van Yperen
 */
@Wire
public class ShipDataSystem extends BaseSystem {

    private ShipLibrary shipLibrary;

    public ShipDataSystem() {
        super();
        loadShips();
    }

    @Override
    protected void processSystem() {
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    private void loadShips() {
        final Json json = new Json();
        shipLibrary = json.fromJson(ShipLibrary.class, Gdx.files.internal("ships.json"));
    }

    public ShipData get( String id ) {
        return shipLibrary.getById(id);
    }
}
