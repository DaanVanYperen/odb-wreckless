package net.mostlyoriginal.game.system.common;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.systems.IteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public abstract class FluidIteratingSystem extends IteratingSystem {

    public FluidIteratingSystem(Aspect.Builder aspect) {
        super(aspect);
    }

    @Override
    protected void process(int id) {
        process(E(id));
    }

    protected abstract void process(E e);
}
