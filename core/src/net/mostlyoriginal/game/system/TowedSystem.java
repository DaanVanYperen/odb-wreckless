package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class TowedSystem extends FluidIteratingSystem {

    GridSnapSystem gridSnapSystem;

    public TowedSystem() {
        super(Aspect.all(Towing.class, SnapToGrid.class));
    }

    Vector2 v = new Vector2();

    public void hookOnto(E e, E towed) {

        E currentlyTowing = getTowing(e);
        releaseTows(e);
        releaseTowed(towed);

        towed.towedEntityId(e.id());
        e.towingEntityId(towed.id());

        // hook car behind this one.
        if ( currentlyTowing != null ) {
            hookOnto(towed, currentlyTowing);
        }
    }

    private void releaseTowed(E towed) {
        final E tower = getTower(towed);
        if (tower != null) {
            releaseTows(tower);
        }
    }

    private E getTower(E towed) {
        return towed.hasTowed() ? E(towed.towedEntityId()) : null;
    }

    private void releaseTows(E e) {
        final E towed = getTowing(e);
        if (towed != null) {
            towed.removeTowed();
        }
        e.removeTowing();
    }

    @Override
    protected void process(E e) {
        final E towed = getTowing(e);
        if (towed != null) {
            gridSnapSystem.moveRelativeToOther(towed, e, -1, 0); // drag behind.
        } else releaseTows(e);
    }

    private E getTowing(E e) {
        return e.hasTowing() ? E(e.towingEntityId()) : null;
    }

}
