package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.render.SpriteLibrary;

import static net.mostlyoriginal.game.component.G.DEBUG_NO_MUSIC;

/**
 * @author Daan van Yperen
 */
@Wire
public class GameScreenAssetSystem extends AbstractAssetSystem {

    private SpriteLibrary spriteLibrary;
    private Music music;
    public static final int TILE_SIZE = 32;

    public GameScreenAssetSystem() {
        super("tileset.png");
        loadSprites();
        loadSounds(
                new String[]{
                }
        );

        Texture tiles = new Texture("tileset.png");

        add("player-idle", 0, 0, TILE_SIZE, TILE_SIZE, 2,1,tileset);
        add("player-jetpack", TILE_SIZE*2, 0, TILE_SIZE, TILE_SIZE, 1,1,tileset);
        add("player-walk", TILE_SIZE*3, 0, TILE_SIZE, TILE_SIZE, 4,1,tileset,0.2f);
        add("player-respawning", TILE_SIZE*7, 0, TILE_SIZE, TILE_SIZE, 1, 1,tileset);

        //playMusicTitle();
    }

    private void playMusicTitle() {
        if (DEBUG_NO_MUSIC) return;
        if (music != null) music.stop();
        music = Gdx.audio.newMusic(Gdx.files.internal("sfx/LD_titleloop.mp3"));
        music.setLooping(true);
        music.play();
        music.setPan(0, 0.1f);

        if (!G.DEBUG_SKIP_INTRO) {
            playSfx("LD_troop_prologue");
        }
    }

    private int musicIndex = 0;
    private String[] musicFiles = new String[]
            {
                    "sfx/music1.mp3",
            };

    public void playMusicInGame() {
        if (DEBUG_NO_MUSIC) return;
        if (music != null) music.stop();
        music = Gdx.audio.newMusic(Gdx.files.internal(musicFiles[musicIndex]));
        music.setLooping(true);
        music.play();
        music.setPan(0, 0.1f);

        musicIndex = (++musicIndex % 3);
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
