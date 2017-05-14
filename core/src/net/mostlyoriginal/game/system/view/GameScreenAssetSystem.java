package net.mostlyoriginal.game.system.view;

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
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.dilemma.CardLibrary;
import net.mostlyoriginal.game.system.planet.SpriteLibrary;

import static net.mostlyoriginal.game.component.G.DEBUG_NO_MUSIC;

/**
 * @author Daan van Yperen
 */
@Wire
public class GameScreenAssetSystem extends AbstractAssetSystem {

    private SpriteLibrary spriteLibrary;
    private Music music;

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

        loadSounds(
                new String[]{
                        "LD1_badthing",
                        "LD1_event1",
                        "LD1_event2",
                        "LD1_event3",
                        "LD1_volcano",
                        "LD1_volcano2",
                        "LD_genuine earth men_jingle",
//                        "LD_titleloop",
                        "LD_troop_amazing",
                        "LD_troop_hello",
                        "LD_troop_no",
                        "LD_troop_prologue",
                        "LD_troop_yeah",
                        "LD_troop_yeah_higher",
                        "LD_lowfi_explosion",
                        "nutrition-coffee",
                        "Politic-Dolphins",
                        "Politic-New Leader",
                        "tech-missiles",
                        "Tech-particle accel",

                        "LD_ghostflipper",
                        "LD_ghostflipper2",
                        "LD_rebornflipper",
                }
        );

        loadLogo();

        playMusicTitle();
        //playMusicInGame();
    }

    private void loadLogo() {
        TextureRegion[] regions = new TextureRegion[16];
        for (int i = 0; i < 16; i++) {
            regions[i] = new TextureRegion(new Texture("index" + (i + 1) + ".png"), G.LOGO_WIDTH, G.LOGO_HEIGHT);
        }

        Animation animation = new Animation<TextureRegion>(0.05f, regions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        sprites.put("logo", animation);
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

    int musicIndex = 0;
    String[] musicFiles = new String[]
            {
                    "sfx/LD1_fortune_teller_loop.mp3",
                    "sfx/LD1_amb_main.mp3",
                    "sfx/LD_titleloop.mp3"
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
