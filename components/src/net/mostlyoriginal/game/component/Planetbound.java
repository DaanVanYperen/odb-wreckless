package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Daan van Yperen
 */
public class Planetbound extends Component {
    public Planetbound() {
    }

    public PlanetCell cell;
    public Vector2 gravity = new Vector2();
}
