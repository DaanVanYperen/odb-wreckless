package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class TowedSystem extends FluidIteratingSystem {

    GridSnapSystem gridSnapSystem;

    public TowedSystem() {
        super(Aspect.all(Towing.class, SnapToGrid.class).exclude(Cashable.class));
    }

    Vector2 v = new Vector2();

    public void hookOnto(E e, E towed) {

        E currentlyTowing = getCargo(e);
        disconnectCargoFrom(e, false);
        disconnectFromTowingCar(towed, false);

        towed.towedEntityId(e.id());
        e.towingEntityId(towed.id());

        // hook car behind this one.
        if (currentlyTowing != null) {
            hookOnto(towed, currentlyTowing);
        }

        disconnectCargoIfChainLongerThan(e, 4);
    }

    private void disconnectCargoIfChainLongerThan(E e, int chainLength) {
        E cargo = e;
        while (cargo != null && cargo.hasTowing()) {
            cargo = getCargo(cargo);
            if (--chainLength == 0) {
                disconnectCargoFrom(cargo, true);
                return;
            }
        }
    }

    public void disconnectFromTowingCar(E towed, boolean violently) {
        final E tower = getTower(towed);
        if (tower != null) {
            disconnectCargoFrom(tower, false);
        }
    }

    private E getTower(E towed) {
        return towed.hasTowed() ? E(towed.towedEntityId()) : null;
    }

    public void disconnectCargoFrom(E e, boolean violently) {
        final E cargo = getCargo(e);
        e.removeTowing();
        if (cargo != null) {
            cargo.removeTowed();
            if (violently) {
                cargo.snapToGridX(cargo.snapToGridX()-3);
                disconnectCargoFrom(cargo, true);
            }
        }
    }

    @Override
    protected void process(E e) {
        final E towed = getCargo(e);
        if (towed != null) {
            gridSnapSystem.moveRelativeToOther(towed, e, -1, 0); // drag behind.
        } else disconnectCargoFrom(e, false);
    }

    private E getCargo(E e) {
        return e.hasTowing() ? E(e.towingEntityId()) : null;
    }

}
