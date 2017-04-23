package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Daan van Yperen
 */
public class PlanetCoord extends Component {
    public PlanetCoord() {
    }

    public PlanetCell cell;
    public Vector2 gravity = new Vector2();
}
