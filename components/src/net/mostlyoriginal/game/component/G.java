package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public class G {
    public static final float CARD_X = 5;
    public static final float CARD_Y = 5;


    public static final int SIMULATION_WIDTH = 220 + 50;
    public static final int SIMULATION_HEIGHT = 220 + 50;
    public static final int MARGIN_BETWEEN_CARDS = 5;

    public static final int LAYER_DUDES = 99;
    public static final int LAYER_GHOST = 100;
    public static final int LAYER_CARDS = 1000;
    public static final int LAYER_CARDS_HOVER = 1001;

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
