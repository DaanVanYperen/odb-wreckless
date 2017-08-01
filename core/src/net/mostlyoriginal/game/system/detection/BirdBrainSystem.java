package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.operation.flow.SequenceOperation;
import net.mostlyoriginal.game.component.BirdBrain;
import net.mostlyoriginal.game.component.Deadly;
import net.mostlyoriginal.game.component.Mortal;
import net.mostlyoriginal.game.component.Robot;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.FollowSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.JamOperationFactory.*;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class BirdBrainSystem extends FluidIteratingSystem {

    public BirdBrainSystem() {
        super(Aspect.all(Pos.class, BirdBrain.class));
    }

    Vector2 v2 = new Vector2();

    @Override
    protected void process(E e) {

        if (e.wallSensorOnFloor() || e.wallSensorOnPlatform()) {
            e.anim(e.birdBrainAnimIdle());
            e.birdBrainFlipCooldown(e.birdBrainFlipCooldown() - world.delta);
            if (e.birdBrainFlipCooldown() < 0) {
                e.birdBrainFlipCooldown(MathUtils.random(1, 5));
                e.animFlippedX(!e.animFlippedX());
            }
            e.physicsFriction(5f);
        } else {
            e.anim(e.birdBrainAnimFlying());
            e.animFlippedX(-e.physicsVx() > 0);
            e.physicsFriction(0);
            if (!e.hasSpooked()) {
                // aim at robot.
                aimAtRobot(e);
            }
        }


        if (e.hasSpooked()) {
            e.spookedCooldown(e.spookedCooldown() - world.delta);
            e.removeGravity();
            if (e.spookedCooldown() <= 0) {
                e.removeSpooked();
                e.gravityY(-0.2f);
                e.script(sequence(tintBetween(Tint.TRANSPARENT, Tint.WHITE, 0.8f)));
            }
        } else if (e.wallSensorOnFloor() || e.wallSensorOnPlatform()) {
            E player = entityWithTag("player");
            if ((player.physicsVx() != 0 || player.physicsVy() != 0) && player.posXy().dst2(e.posXy()) < 64 * 64) {
                e.spooked();
                e.spookedCooldown(3 + MathUtils.random(2f));
                e.script(sequence(tintBetween(Tint.WHITE, Tint.TRANSPARENT, 0.8f)));
                e.physicsVx(MathUtils.random(-100, 100) * 0.25f);
                e.physicsVy(MathUtils.random(200, 250) * 0.25f);
            }
        }
    }

    private void aimAtRobot(E e) {
        E robot = entityWithTag("robot");
        float targetY = robot.posY() + robot.boundsCy();
        if (targetY < e.posY() && robot.posXy().dst2(e.posXy()) < 500 * 500) { // only when bird ABOVE robot.
            v2.set(robot.posX() + robot.boundsCx() * e.birdBrainFavoriteSpot(), targetY).sub(e.posX(), e.posY()).scl(0.5f);
            e.physicsVy(v2.y);
            e.physicsVx(v2.x);
        }
    }
}
