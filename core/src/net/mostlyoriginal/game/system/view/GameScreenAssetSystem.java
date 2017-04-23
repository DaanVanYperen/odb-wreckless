package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.dilemma.CardLibrary;
import net.mostlyoriginal.game.system.planet.SpriteLibrary;

/**
 * @author Daan van Yperen
 */
@Wire
public class GameScreenAssetSystem extends AbstractAssetSystem {

    private SpriteLibrary spriteLibrary;

    public GameScreenAssetSystem() {
        super("cards.png");
        loadSprites();

        // recycle!
        int offsetY = 140;
        add("star-0-0", 32, 136 + offsetY, 4, 4, 1);
        add("star-0-1", 32 - 4, 136 + offsetY, 4, 4, 1);
        add("star-0-2", 32 - 8, 136 + offsetY, 4, 4, 1);
        add("star-0-3", 40, 136 + offsetY, 7, 4, 1);
        add("star-0-4", 48, 136 + offsetY, 26, 4, 1);
        add("star-0-5", 80, 136 + offsetY, 36, 4, 1);

        add("star-1-0", 32, 144 + offsetY, 2, 2, 1);
        add("star-1-1", 32 - 4, 144 + offsetY, 2, 2, 1);
        add("star-1-2", 32 - 8, 144 + offsetY, 2, 2, 1);
        add("star-1-3", 40, 144 + offsetY, 6, 2, 1);
        add("star-1-4", 48, 144 + offsetY, 12, 2, 1);
        add("star-1-5", 80, 144 + offsetY, 21, 2, 1);

        add("star-2-0", 32, 152 + offsetY, 1, 1, 1);
        add("star-2-1", 32 - 4, 152 + offsetY, 1, 1, 1);
        add("star-2-2", 32 - 8, 152 + offsetY, 1, 1, 1);
        add("star-2-3", 40, 152 + offsetY, 4, 1, 1);
        add("star-2-4", 48, 152 + offsetY, 7, 1, 1);
        add("star-2-5", 80, 152 + offsetY, 15, 1, 1);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    private void loadSprites() {
        final Json json = new Json();
        spriteLibrary = json.fromJson(SpriteLibrary.class, Gdx.files.internal("sprites.json"));
        for (SpriteData sprite : spriteLibrary.sprites) {
            add(sprite.id, sprite.x, sprite.y, sprite.width, sprite.height, sprite.countX, sprite.countY);
        }
    }

}
