package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Exit;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;

/**
 * @author Daan van Yperen
 */
public class ExitSystem extends FluidIteratingSystem {

    public TransitionSystem transitionSystem;

    public ExitSystem() {
        super(Aspect.all(Exit.class, Pos.class));
    }

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        E robot = entityWithTag("robot");

        if ( overlaps(robot, e) ) {
            if ( robot.chargeCharge() < G.BARS_NEEDED_FOR_BREAKING_DOOR ) {
                robot.needsBatteries();
            } else {
                e.chargeCooldown(e.chargeCooldown -= world.delta);
                if ( e.chargeCooldown() < 2 && !e.chargeBroken()) {
                    e.chargeBroken(true);
                    E.E().pos(e).animId()
                }
                if ( e.chargeCooldown() < 1 && !e.chargeOpen()) {
                    e.chargeOpen(true);
                }
            }
        } else robot.removeNeedsBatteries();

        if ( e.chargeOpen() && overlaps(player, e) && overlaps(robot, e)) {
            doExit(e);
        }
    }

    private void doExit(E e) {
        e.removeExit();
        G.level++;
        transitionSystem.transition(GameScreen.class, 0.1f);
    }
}
