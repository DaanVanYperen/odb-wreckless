package net.mostlyoriginal.game.system.common;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import net.mostlyoriginal.game.api.EBag;

import java.util.Iterator;

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


    protected EBag allEntitiesMatching(Aspect.Builder scope) {
        return new EBag(world.getAspectSubscriptionManager().get(scope).getEntities());
    }

    protected EBag allEntitiesWith(Class<? extends Component> scope) {
        return new EBag(world.getAspectSubscriptionManager().get(Aspect.all(scope)).getEntities());
    }

    protected E entityWithTag(String tag) {
        return E(world.getSystem(TagManager.class).getEntity(tag));

    }
}
