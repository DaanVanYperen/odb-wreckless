package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.JumpAttack;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class JumpAttackSystem extends FluidIteratingSystem {
    public JumpAttackSystem() {
        super(Aspect.all(JumpAttack.class));
    }

    Vector2 v2 = new Vector2();

    @Override
    protected void process(E e) {
        E robot = entityWithTag("robot");
        E player = entityWithTag("player");

        E target = robot.chargeCharge() > 0 ? robot : player;

        if (target != null) {
            v2.set(target.posX(), target.posY()).sub(e.posX(), e.posY()).nor().scl(300);

            if (e.wallSensorOnFloor() || e.wallSensorOnPlatform()) {
                e.physicsVx(MathUtils.clamp(v2.x, -200, 200));
                e.physicsVy(MathUtils.clamp(v2.y, 80, 500));
            }

            e.animFlippedX(target.posX() + e.boundsCx() < e.posX() + e.boundsCx());
        }
    }
}
