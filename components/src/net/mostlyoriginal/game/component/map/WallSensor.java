package net.mostlyoriginal.game.component.map;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class WallSensor extends Component {
    public boolean onFloor = false;
    public boolean onHorizontalSurface = false;
    public boolean onVerticalSurface = false;
    public boolean onPlatform = false;

    public float wallAngle;

    public WallSensor() {
    }

    public boolean onAnySurface() {
        return onHorizontalSurface || onVerticalSurface || onPlatform;
    }
}