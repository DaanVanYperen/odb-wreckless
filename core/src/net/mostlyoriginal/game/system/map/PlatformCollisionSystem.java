package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import jdk.nashorn.internal.ir.Terminal;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.Platform;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.*;

/**
 * @author Daan van Yperen
 */
public class PlatformCollisionSystem extends FluidIteratingSystem {

    private static boolean DEBUG = false;

    private MapSystem mapSystem;
    private CameraSystem cameraSystem;

    private boolean initialized;
    private MapMask solidMask;

    public PlatformCollisionSystem() {
        super(Aspect.all(Physics.class, Pos.class, Bounds.class).exclude(Platform.class));
    }

    @Override
    protected void begin() {
        if (!initialized) {
            initialized = true;
            solidMask = mapSystem.getMask("solid");
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(E e) {
        final Physics physics = e.getPhysics();
        final Pos pos = e.getPos();
        final Bounds bounds = e.getBounds();

        //  no math required here.
        e.wallSensorOnPlatform(false);
        if (physics.vy < 0) {

            float px = pos.xy.x + physics.vx * world.delta;
            float py = pos.xy.y + physics.vy * world.delta;

            if (collides(px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.miny)) {
                physics.vy = physics.bounce > 0 ? -physics.vy * physics.bounce : 0;
                e.wallSensorOnPlatform(true);
            }

        }

    }

    private boolean collides(final float x, final float y) {
         //E().pos(x - 1, y - 1).anim("player-idle").render(2000);

        for (E e : allEntitiesMatching(Aspect.all(Platform.class))) {
            if (overlaps(e, x, y)) return true;
        }

        return false;
    }


    public final boolean overlaps(final E a, float x, float y) {

        final Bounds b1 = a.getBounds();
        final Pos p1 = a.getPos();

        if (b1 == null || p1 == null)
            return false;

        final float minx = p1.xy.x + b1.minx;
        final float miny = p1.xy.y + b1.miny;
        final float maxx = p1.xy.x + b1.maxx;
        final float maxy = p1.xy.y + b1.maxy;

        final float bminx = x;
        final float bminy = y;
        final float bmaxx = x;
        final float bmaxy = y;

        return
                !(minx > bmaxx || maxx < bminx ||
                        miny > bmaxy || maxy < bminy);
    }
}
