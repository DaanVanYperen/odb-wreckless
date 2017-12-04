package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class FlightPatternStep implements Serializable {
    public enum Step {
        FLY
    }
    public Step step;
    public float angle;
    public float seconds;
}
