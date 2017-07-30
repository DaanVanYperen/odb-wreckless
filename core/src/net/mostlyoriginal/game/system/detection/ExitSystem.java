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

        if (overlaps(player, e) && overlaps(robot, e)) {
            e.removeExit();
            G.level++;
            transitionSystem.transition(GameScreen.class, 0.1f);
        }
    }
}
