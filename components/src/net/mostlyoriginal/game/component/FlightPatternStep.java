package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class FlightPatternStep implements Serializable {
    public enum Step {
        FLY,
        EXPLODE, HIDE_LEFT, HIDE_RIGHT, FACE_PLAYER, FLY_SINUS, SPIN, PAUSE
    }
    public Step step;
    public float angle=0;
    public float facing=0;
    public float seconds=999;
}
