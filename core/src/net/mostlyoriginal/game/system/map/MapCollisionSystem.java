package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.systems.EntityProcessingSystem;
import jdk.nashorn.internal.ir.Terminal;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class MapCollisionSystem extends FluidIteratingSystem {

    private static boolean DEBUG = false;

    private MapSystem mapSystem;
    private CameraSystem cameraSystem;

    private boolean initialized;
    private MapMask solidMask;

    public MapCollisionSystem() {
        super(Aspect.all(Physics.class, Pos.class, Bounds.class));
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
        if (physics.vx != 0 || physics.vy != 0) {

            float px = pos.xy.x + physics.vx * world.delta;
            float py = pos.xy.y + physics.vy * world.delta;

            if ((physics.vx > 0 && collides(px + bounds.maxx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.5f)) ||
                    (physics.vx < 0 && collides(px + bounds.minx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.5f))) {
                physics.vx = physics.bounce > 0 ? -physics.vx * physics.bounce : 0;
                px = pos.xy.x;
            }

            if ((physics.vy > 0 && collides(px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.maxy)) ||
                    (physics.vy < 0 && collides(px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.miny))) {
                physics.vy = physics.bounce > 0 ? -physics.vy * physics.bounce : 0;
            }

        }

    }

    private boolean collides(final float x, final float y) {
        if (DEBUG) {
//            world.createEntity()
//                    .edit()
//                    .add(new Pos(x - 1, y - 1))
//                    .add(new Anim("debug-marker"))
//                    .add(new Terminal(1))
//            ;
        }

        return solidMask.atScreen(x, y, true);
    }
}
