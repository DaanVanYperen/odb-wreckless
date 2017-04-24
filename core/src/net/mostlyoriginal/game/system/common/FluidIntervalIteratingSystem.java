package net.mostlyoriginal.game.system.common;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.systems.IntervalIteratingSystem;
import com.artemis.systems.IteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public abstract class FluidIntervalIteratingSystem extends IntervalIteratingSystem {

    public FluidIntervalIteratingSystem(Aspect.Builder aspect, float interval) {
        super(aspect, interval);
    }

    @Override
    protected void process(int id) {
        process(E(id));
    }

    protected abstract void process(E e);
}
