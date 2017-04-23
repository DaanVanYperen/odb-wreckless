package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public class StatusMask {
    public static final float ARID_TEMPERATURE = 300;
    public static float MAX_TEMPERATURE = 300;
    public float temperature=0;

    public void reset() {
        temperature=0;
    }
}
