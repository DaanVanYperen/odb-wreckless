package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class Spout extends Component {
    public float angle;
    public float cooldown = 2f;
    public float age = 0;
    public float sprayDuration = 10f;
    public float sprayInterval = 6f/32f;
    public float sprayCooldown = 0f;
    public Type type = Type.BULLET;
    public enum Type {
        BULLET,
        GREMLIN,
        ACID
    }
    public Spout() {
    }
}
