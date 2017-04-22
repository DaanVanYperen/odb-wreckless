package net.mostlyoriginal.game.system.common;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public abstract class FluidDeferredEntityProcessingSystem extends DeferredEntityProcessingSystem {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     *
     * @param aspect    to match against entities
     * @param principal principal that will organize process calls to this system.
     */
    public FluidDeferredEntityProcessingSystem(Aspect.Builder aspect, EntityProcessPrincipal principal) {
        super(aspect, principal);
    }

    @Override
    protected void process(int e) {
        process(E(e));
    }

    protected abstract void process(E e);
}
