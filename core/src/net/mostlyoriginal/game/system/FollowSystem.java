package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.component.Follow;
import net.mostlyoriginal.game.component.Pickup;
import net.mostlyoriginal.game.component.PlayerControlled;
import net.mostlyoriginal.game.component.Socket;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;

/**
 * @author Daan van Yperen
 */
public class FollowSystem extends FluidIteratingSystem {
    public static final int ALLOWED_DISTANCE = 10;
    private float MOVEMENT_FACTOR = 100;
    private float JUMP_FACTOR = 0;
    private MapCollisionSystem mapCollision;

    public FollowSystem() {
        super(Aspect.all(Follow.class, Physics.class, WallSensor.class, Anim.class));
    }

    @Override
    protected void process(E e) {

        e.animId("robot-idle");
        e.angleRotation(0);
        e.physicsVr(0);

        E following = entityWithTag("player");

        float dx = 0;
        float dy = 0;

        if (following.posX() < e.posX() - ALLOWED_DISTANCE) {
            dx = -MOVEMENT_FACTOR;
            e.animFlippedX(true);
        }
        if (following.posX() > e.posX() + ALLOWED_DISTANCE) {
            dx = MOVEMENT_FACTOR;
            e.animFlippedX(false);
        }

        Bounds bounds = e.getBounds();
        if ( !mapCollision.collides(e.posX() + (dx < 0 ? bounds.minx-4 : bounds.maxx+4), e.posY() - 4)) {
            dx=0;
            e.physicsVx(0);
        }

        if (dx != 0) {
            e.physicsVx(e.physicsVx() + (dx * world.delta));
            e.animId("robot-walk");
        }
    }
}
