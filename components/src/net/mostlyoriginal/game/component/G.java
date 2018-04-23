package net.mostlyoriginal.game.component;

import com.badlogic.gdx.Gdx;
import net.mostlyoriginal.game.client.SfxHandler;
import net.mostlyoriginal.game.client.UriHandler;

/**
 * @author Daan van Yperen
 */
public class G {
    public static final float ROBOT_HOVER_ABOVE_PLAYER_HEIGHT = 8;
    public static final float BARS_NEEDED_FOR_BREAKING_DOOR = 3;
    public static final int LAYER_PARTICLES = 2000;
    public static final int LAYER_GREMLIN = 998;
    public static final int LAYERS_RESERVED_FOR_CARS = 300;
    public static final int TEAM_PLAYERS = 2;
    public static final int TEAM_ENEMIES = 0;
    public static final int LAYER_BIRDS = 1005;
    public static final int LAYER_DIALOGS = 2100;
    public static final int PLAYER_WIDTH = 46;
    public static final int PLAYER_HEIGHT = 39;
    public static final float CAMERA_SCROLL_SPEED = 40;
    public static Settings settings;

    public static final boolean PRODUCTION = false;

    public static final boolean DEBUG_SKIP_MUSIC = (!PRODUCTION && false);
    public static final boolean DEBUG_NO_MUSIC = (!PRODUCTION && false);
    public static final int CELL_SIZE = 32;


    public static final int LAYER_PLAYER = 1500;
    public static final int LAYER_PLAYER_ROBOT = 996;
    public static final int LAYER_PLAYER_ROBOT_BATTERY = 997;

    // 1280 x 720

    public static int CAMERA_ZOOM = 2;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    public static int level = 0;
    public static float ROBOT_FLY_ABOVE_PLAYER_HEIGHT = 8;

    public static final float BAR_PER_SECOND_LOST_FOR_FLYING = 0.05f;
    public static final float BAR_PER_SECOND_LOST_FOR_HOVERING = 0.25f;
    public static final float BAR_PER_SECOND_LOST_FOR_WALKING = 0.01f;
    public static final float BARS_FOR_BATTERY = 3.5f;
    public static int LAYER_DOOR = 990;
    public static UriHandler net = new UriHandler() {
        public void openURI(String URI) {
            Gdx.net.openURI(URI);
        }
    };
    public static SfxHandler sfx;
}
