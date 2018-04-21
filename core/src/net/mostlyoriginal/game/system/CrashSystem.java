package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.Crashable;
import net.mostlyoriginal.game.component.SnapToGrid;
import net.mostlyoriginal.game.component.Towed;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class CrashSystem extends FluidIteratingSystem {

    private EBag onGrid;

    public CrashSystem() {
        super(Aspect.all(Towed.class));
    }

    @Override
    protected void begin() {
        super.begin();
//        onGrid = allEntitiesWith(Crashable.class);
    }

    @Override
    protected void process(E e) {
//        if (world.delta == 0) return;
//        for (E other : onGrid) {
//            if (other != e && overlaps(other, e) && !other.hasTowed()) {
//                e.deleteFromWorld();
//                break;
//            }
//        }
    }

}
