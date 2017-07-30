package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Exit;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Trigger;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;

/**
 * @author Daan van Yperen
 */
public class TriggerSystem extends FluidIteratingSystem {

    public TriggerSystem() {
        super(Aspect.all(Trigger.class, Pos.class));
    }

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        E robot = entityWithTag("robot");

        if (overlaps(player, e) || overlaps(robot, e)) {
            switch (e.triggerTrigger()) {
                case "start-running":
                    player.running();
                    player.removeCameraFocus();
                    E.E().pos(player.getPos()).cameraFocus().physicsFriction(0).physicsVx(100).tag("pacer");
                    robot.running();
                    break;
                case "stop-running":
                    player.removeRunning();
                    player.cameraFocus();
                    robot.removeRunning();
                    entityWithTag("pacer").deleteFromWorld();
                    break;
                case "robot-land":
                    robot.flying();
                    break;
                case "robot-hover":
                    robot.removeFlying();
                    break;
            }
            e.removeTrigger();
        }
    }
}
