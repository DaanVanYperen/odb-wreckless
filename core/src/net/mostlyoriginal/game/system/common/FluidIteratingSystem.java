package net.mostlyoriginal.game.system.common;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
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

    protected E firstTouchingEntityMatching(E subject, Aspect.Builder scope) {
        for (E e : allEntitiesMatching(scope)) {
            if (overlaps(e, subject))
                return e;
        }
        return null;
    }

    public final boolean overlaps(final E a, final E b) {
        final Bounds b1 = a.getBounds();
        final Pos p1 = a.getPos();
        final Bounds b2 = b.getBounds();
        final Pos p2 = b.getPos();

        if (b1 == null || p1 == null || b2 == null || p2 == null)
            return false;

        final float minx = p1.xy.x + b1.minx;
        final float miny = p1.xy.y + b1.miny;
        final float maxx = p1.xy.x + b1.maxx;
        final float maxy = p1.xy.y + b1.maxy;

        final float bminx = p2.xy.x + b2.minx;
        final float bminy = p2.xy.y + b2.miny;
        final float bmaxx = p2.xy.x + b2.maxx;
        final float bmaxy = p2.xy.y + b2.maxy;

        return
                !(minx > bmaxx || maxx < bminx ||
                        miny > bmaxy || maxy < bminy);
    }

    protected E entityWithTag(String tag) {
        final Entity entity = world.getSystem(TagManager.class).getEntity(tag);
        return entity != null ? E(entity) : null;

    }
}
