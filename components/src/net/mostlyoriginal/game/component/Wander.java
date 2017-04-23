package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

import static net.mostlyoriginal.game.component.Wander.Direction.LEFT;
import static net.mostlyoriginal.game.component.Wander.Direction.RIGHT;

/**
 * @author Daan van Yperen
 */
public class Wander extends Component {
    public Wander() {
    }

    public Direction direction = MathUtils.randomBoolean() ? LEFT : RIGHT;

    enum Direction {
        LEFT,
        RIGHT;
    }
}
