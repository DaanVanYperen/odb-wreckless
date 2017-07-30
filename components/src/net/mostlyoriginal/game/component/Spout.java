package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class Spout extends Component {
    public float angle;
    public float cooldown = 8;
    public float age = MathUtils.random(6f);
    public float sprayDuration = 4;
    public float sprayInterval = 0.04f;
    public float sprayCooldown = 0;
    public Type type = Type.ACID;
    public enum Type {
        ACID,
        GREMLIN
    }
    public Spout() {
    }
}
