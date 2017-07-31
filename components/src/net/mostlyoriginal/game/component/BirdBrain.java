
package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class BirdBrain extends Component {
    public String animIdle;
    public String animFlying;
    public float flipCooldown;
    public float favoriteSpot = MathUtils.random(0.56f,0.6f);
    public BirdBrain() {
    }
}
