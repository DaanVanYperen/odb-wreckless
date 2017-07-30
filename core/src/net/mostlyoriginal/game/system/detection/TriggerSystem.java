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

        boolean robotOverlaps = overlaps(robot, e);
        if (overlaps(player, e) || robotOverlaps) {
            switch (e.triggerTrigger()) {
                case "start-running":
                    player.running();
                    player.removeCameraFocus();
                    E.E().pos(player.getPos()).cameraFocus().physicsFriction(0).physicsVx(100).tag("pacer");
                    robot.running();
                    e.removeTrigger();
                    break;
                case "stop-running":
                    player.removeRunning();
                    player.cameraFocus();
                    robot.removeRunning();
                    entityWithTag("pacer").deleteFromWorld();
                    e.removeTrigger();
                    break;
                case "robot-land":
                    if ( robotOverlaps ) {
                        robot.removeFlying();
                        e.removeTrigger();
                    }
                    break;
                case "robot-hover":
                    if ( robotOverlaps ) {
                        robot.flying();
                        e.removeTrigger();
                    }
                    break;
            }
        }
    }
}
