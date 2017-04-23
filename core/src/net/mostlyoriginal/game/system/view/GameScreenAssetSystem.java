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
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    private void loadSprites() {
        final Json json = new Json();
        spriteLibrary = json.fromJson(SpriteLibrary.class, Gdx.files.internal("sprites.json"));
        for (SpriteData sprite : spriteLibrary.sprites) {
            add(sprite.id, sprite.x, sprite.y, sprite.width,sprite.height, sprite.countX, sprite.countY);
        }
    }

}
