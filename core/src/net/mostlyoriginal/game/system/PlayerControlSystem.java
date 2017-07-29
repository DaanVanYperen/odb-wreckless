package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.game.component.Pickup;
import net.mostlyoriginal.game.component.PlayerControlled;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class PlayerControlSystem extends FluidIteratingSystem {
    private float MOVEMENT_FACTOR = 500;
    private float JUMP_FACTOR = 15000;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerControlled.class, Physics.class, WallSensor.class, Anim.class));
    }

    @Override
    protected void process(E e) {

        e.animId("player-idle");
        e.angleRotation(0);
        e.physicsVr(0);

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -MOVEMENT_FACTOR;
            e.animFlippedX(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = MOVEMENT_FACTOR;
            e.animFlippedX(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && (e.wallSensorOnFloor() || e.wallSensorOnPlatform())) {
            dy = JUMP_FACTOR;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (e.hasCarries()) {
                dropAllCarried(e);
            } else {
                E pickup = firstTouchingEntityMatching(e, Aspect.all(Pickup.class));
                if (pickup != null) {
                    carryItem(e, pickup);
                } else {
                    callRobot(e);
                }
            }
        }

        if (dx != 0) {
            e.physicsVx(e.physicsVx() + (dx * world.delta));
            e.animId("player-walk");
        }
        if (dy != 0) {
            e.physicsVy((dy * world.delta));
        }
    }

    private void dropAllCarried(E e) {
        if (e.hasCarries()) {
            E.E(e.getCarries().entityId).gravity();
            e.removeCarries();
        }
    }

    private void carryItem(E e, E pickup) {
        e.carriesEntityId(pickup.id());
        e.carriesAnchorY((int)e.boundsMaxy());
        pickup.removeGravity();
    }

    private void callRobot(E e) {
        E robot = entityWithTag("robot");
        robot.animFlippedX(e.animFlippedX());
        robot.pos(e.posX(), e.posY());
    }
}
