package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.E;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.Crashable;
import net.mostlyoriginal.game.component.SnapToGrid;
import net.mostlyoriginal.game.component.Spinout;
import net.mostlyoriginal.game.component.Towed;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class CrashSystem extends BaseEntitySystem {

    private EBag onGrid;

    public CrashSystem() {
        super(Aspect.all(Crashable.class).exclude(Towed.class, Frozen.class));
    }

    @Override
    protected void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            for (int j = i + 1; s > j; j++) {
                crashTest(E(ids[i]), E(ids[j]));
            }
        }
    }

    Vector2 v = new Vector2();

    private void crashTest(E a, E b) {
        if (overlaps(a, b)) {

            if ( a.hasSpinout() && b.hasSpinout() ) return;


            final float angle = v.set(a.posX() + a.boundsCx(), a.posY() + a.boundsCy())
                    .sub(b.posX() + b.boundsCx(), b.posY() + b.boundsCy()).angle();

            a.spinout();
            b.spinout();

            // Avoid taking control away from the player for too long.
            a.spinoutSpeed(a.hasShipControlled() ? MathUtils.random(2f, 3f) : MathUtils.random(1f, 2f));
            b.spinoutSpeed(b.hasShipControlled() ? MathUtils.random(2f, 3f) : MathUtils.random(1f, 2f));

            a.spinoutDirection(angle);
            b.spinoutDirection(angle + 180);
            a.spinoutAngle(MathUtils.random(400,600) * (MathUtils.randomBoolean() ? 1 : -1));
            b.spinoutAngle(MathUtils.random(400,600) * (MathUtils.randomBoolean() ? 1 : -1));
        }
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

}
