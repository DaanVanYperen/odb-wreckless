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
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Towed;

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

            if (a.hasSpinout() && b.hasSpinout()) return;


            float angle = v.set(a.posX() + a.boundsCx(), a.posY() + a.boundsCy())
                    .sub(b.posX() + b.boundsCx(), b.posY() + b.boundsCy()).angle();

            if ( a.hasHazard() || b.hasHazard() ) angle = MathUtils.random(360);

            if ( !a.hasSpinout() || !b.hasSpinout()) {
                //G.sfx.play(a.hasOilslick() || b.hasOilslick() ? "carsound_oilskid_1" : "carsound_skid_1");
            }

            // hazards don't cause spinouts (yet).
            if (!a.hasHazard()) spinOut(b, angle + 180);
            if (!b.hasHazard()) spinOut(a, angle);
        }
    }

    private void spinOut(E e, float v) {
        if ( e.hasOilslick() ) return;
        if (e.hasSpinout() && e.spinoutFactor() < ( e.hasHazard() ? 0.5f : 0.3f) ) return;
        e.spinout();
        e.spinoutSpeed(e.hasShipControlled() ? MathUtils.random(2.5f, 3f) : MathUtils.random(2.5f, 3f));
        e.spinoutDirection(v);
        e.spinoutAngle(MathUtils.random(400, 600) * (MathUtils.randomBoolean() ? 1 : -1));
        knockDownHazard(e);
        if ( e.hasHazard() && e.hazardHitSound() != null ) {
            G.sfx.play(e.hazardHitSound(),1.5f);
            e
                    .removeHazard()
                    .removeCrashable()
                    .removeSnapToGrid();

        }
    }

    private void knockDownHazard(E e) {
        if (e.hasHazard()) {
            if ( !e.hazardDown() ) {
                e.anim(e.hazardSpriteDown());
                e.hazardDown(true);
            } else {
                if ( MathUtils.random(0,100) < 5 ) {
                    e.anim(e.hazardSpriteUp());
                    e.hazardDown(false);
                }
            }
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
