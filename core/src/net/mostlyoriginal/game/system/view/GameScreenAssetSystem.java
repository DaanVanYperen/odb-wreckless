package net.mostlyoriginal.game.system.view;

import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.client.SfxHandler;
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
    public static final int SMALL_TILE_SIZE = 16;
    public static final int GIANT_TILE_SIZE = 48;

    public GameScreenAssetSystem() {
        super("tileset.png");
        loadSprites();
        loadSounds(
                new String[]{
                        //"ann_breakdown_1",
                        //"ann_go_1",
                        //"ann_go_2",
                        //"ann_go_3",
                        //"ann_triple_1",
                        "carsound_accel_1",
                        "carsound_accel_2",
                        "carsound_carpass_1",
                        "carsound_carpass_2",
                        "carsound_carpass_3",
                        "carsound_oilskid_1",
                        "carsound_skid_1",
                        "countdown_3",
                        //"crash",
                        "crash_1",
                        "crash_2",
                        "FAIL",
                        //"frog_bleh_1",
                        "frog_godwhy",
                        //"frog_mylegs_1",
                        //"frog_mylegs_2",
                        //"misc_1",
                        //"misc_2",
                        //"misc_3",
                        "pop_1", // used
                        "pop_2", // used
                        "rewardsound_1", // used
                        "rewardsound_2",
                        "rewardsound_3", // used
                        "truck_revdown",
                        "truck_revhigh",
                        "truck_revlow",
                        "truck_revup",
                        "truck_revdown_fast",
                        "truck_revup_fast",
                        //"wreck_happy",
                }
        );

        Texture tiles = new Texture("tileset.png");

        //playMusicTitle();
        G.sfx = new SfxHandler() {
            @Override
            public void play(String sfx) {
                GameScreenAssetSystem.this.playSfx(sfx);
            }
            @Override
            public void play(String sfx, float volumeFactor) {
                GameScreenAssetSystem.this.playSfx(sfx,volumeFactor);
            }

            @Override
            public void playDelayed(String sfx, float delay) {
                E.E().sfxName(sfx).sfxCooldown(delay);
            }
        };
    }

    private void playMusicTitle() {
        if (DEBUG_NO_MUSIC) return;
        if (music != null) music.stop();
        music = Gdx.audio.newMusic(Gdx.files.internal("sfx/something1.mp3"));
        music.setLooping(true);
        music.play();
        music.setPan(0, 0.1f);

    }

    public void playMusicInGame(String song) {
        if (DEBUG_NO_MUSIC) return;
        if (music != null) music.stop();
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/" + song));
        music.setLooping(true);
        music.play();
        music.setPan(0, 0.08f);
    }

    public void stopMusic() {
        if (music != null) music.stop();
    }

    @Override
    protected void initialize() {
        super.initialize();
        if (!G.DEBUG_SKIP_MUSIC) {
           // playMusicInGame("wrecklessblues.mp3");
        }
    }

    private void loadSprites() {
        final Json json = new Json();
        spriteLibrary = json.fromJson(SpriteLibrary.class, Gdx.files.internal("sprites.json"));
        for (SpriteData sprite : spriteLibrary.sprites) {
            Animation animation = add(sprite.id, sprite.x, sprite.y, sprite.width, sprite.height, sprite.countX, sprite.countY, this.tileset, sprite.milliseconds * 0.001f);
            if (!sprite.repeat) animation.setPlayMode(Animation.PlayMode.NORMAL);
        }
    }

    public void playSfx(String name, float sfxFactor) {
        if (sfxVolume > 0) {
            Sound sfx = getSfx(name);
            if (sfx != null) {
                sfx.stop();
                sfx.play(sfxVolume * sfxFactor, 1, 0);
            }
        }
    }

    public void boundToAnim(int entity, int gracepaddingX, int gracepaddingY) {
        E e = E.E(entity);
        TextureRegion frame = ((Animation<TextureRegion>) get(e.animId())).getKeyFrame(0);
        e.bounds(gracepaddingX, gracepaddingY, frame.getRegionWidth() - gracepaddingX, frame.getRegionHeight() - gracepaddingY);
    }
}
