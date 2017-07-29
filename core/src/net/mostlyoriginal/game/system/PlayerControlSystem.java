package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.game.component.PlayerControlled;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class PlayerControlSystem extends FluidIteratingSystem {
    private float MOVEMENT_FACTOR = 1500;
    private float JUMP_FACTOR = 10000;

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

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = MOVEMENT_FACTOR;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) // jump
        {
            dy = JUMP_FACTOR;
        }
        ;

        if (dx != 0) {
            e.physicsVx(e.physicsVx() + (dx * world.delta));
            e.animId("player-walk");
        }
        if (dy != 0) {
            e.physicsVy(e.physicsVy() + (dy * world.delta));
        }
    }
}
