package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public class G {

    public static final int LOGO_WIDTH = 280;
    public static final int LOGO_HEIGHT = 221;
    public static final int LAYER_CURSOR = 2000;

    public static final boolean PRODUCTION = true;

    public static final boolean INTERLACING_SIMULATION = true;

    public static final boolean DEBUG_SKIP_INTRO = (!PRODUCTION && true);
    public static final boolean DEBUG_ACHIEVEMENTS = (!PRODUCTION && true);
    public static final boolean DEBUG_DRAWING = (!PRODUCTION && true);
    public static final boolean DEBUG_NO_ENTITY_RENDERING =(!PRODUCTION && true);
    public static final boolean DEBUG_NO_SECOND_PASS  =(!PRODUCTION && false);
    public static boolean DEBUG_NO_FLOW = (!PRODUCTION && false);
    public static boolean DEBUG_AIR_PLANET = (!PRODUCTION && true);
    public static boolean DEBUG_NAIVE_GRAVITY = (!PRODUCTION && false);

    public static final float CARD_X = 5;
    public static final float CARD_Y = 5;


    public static final int SIMULATION_WIDTH = 220 + 50;
    public static final int SIMULATION_HEIGHT = 220 + 50;
    public static final int MARGIN_BETWEEN_CARDS = 5;

    public static final int LAYER_STAR = -50;
    public static final int LAYER_STRUCTURES_BACKGROUND = -2;
    public static final int LAYER_PLANET = 0;
    public static final int LAYER_DUDES = 99;
    public static final int LAYER_GHOST = 100;
    public static final int LAYER_CARDS = 1000;
    public static final int LAYER_CARDS_HOVER = 1001;
    public static final int LAYER_CARDS_FLYING = 1002;
    public static final int LAYER_ACHIEVEMENTS = 900;
    public static final int LAYER_STRUCTURES = -1;
    public static final int LAYER_STRUCTURES_FOREGROUND = 101;

    public static int CAMERA_ZOOM = 2;
    public static final int SCREEN_WIDTH = SIMULATION_WIDTH * 2 * CAMERA_ZOOM;
    private static int CARD_HEIGHT = 90;
    private static int MARGIN_BETWEEN_CARD_AND_SIM = 10;

    public static int PLANET_X = (SIMULATION_WIDTH) - (SIMULATION_WIDTH / 2);
    public static int PLANET_Y = MARGIN_BETWEEN_CARD_AND_SIM + CARD_HEIGHT;
    private static final int MARGIN_BETWEEN_SIM_AND_ROOF = 20;
    public static final int SCREEN_HEIGHT = (SIMULATION_HEIGHT + CARD_HEIGHT + MARGIN_BETWEEN_CARD_AND_SIM + MARGIN_BETWEEN_SIM_AND_ROOF) * CAMERA_ZOOM;

    public static final int PLANET_CENTER_X = PLANET_X + (SIMULATION_WIDTH / 2);
    public static final int PLANET_CENTER_Y = PLANET_Y + (SIMULATION_HEIGHT / 2);

    public static final int GRADIENT_SCALE = 5;
}
