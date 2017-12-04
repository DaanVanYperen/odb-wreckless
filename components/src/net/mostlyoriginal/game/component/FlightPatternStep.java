package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class FlightPatternStep implements Serializable {
    public enum Step {
        FLY,
        FLY_SINUS
    }
    public Step step;
    public float angle;
    public float facing;
    public float seconds;
}
