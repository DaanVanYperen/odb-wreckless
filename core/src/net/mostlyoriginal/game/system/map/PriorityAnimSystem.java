package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.game.component.PriorityAnim;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class PriorityAnimSystem extends FluidIteratingSystem {
    public PriorityAnimSystem() {
        super(Aspect.all(PriorityAnim.class));
    }

    @Override
    protected void process(E e) {
        e.priorityAnimCooldown(e.priorityAnimCooldown() - world.delta);
        if (e.priorityAnimCooldown() <= 0) {
            e.removePriorityAnim();
        } else {
            e.animId(e.priorityAnimAnimId());
            e.animAge(e.priorityAnimAge());
            e.priorityAnimAge(e.priorityAnimAge() + world.delta);
        }
    }
}
