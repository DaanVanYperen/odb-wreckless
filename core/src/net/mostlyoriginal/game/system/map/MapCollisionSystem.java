package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.Ethereal;
import net.mostlyoriginal.game.component.Flying;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class MapCollisionSystem extends FluidIteratingSystem {

    public static boolean DEBUG = false;

    private MapSystem mapSystem;
    private CameraSystem cameraSystem;

    private boolean initialized;
    private MapMask solidMask;
    protected MapMask canHoverMask;
    protected MapMask deadlyMask;
    protected MapMask solidForRobotMask;

    private Color RED = Color.valueOf("FF0000FF");

    public MapCollisionSystem() {
        super(Aspect.all(Physics.class, Pos.class, Bounds.class).exclude(Flying.class, Ethereal.class));
    }

    @Override
    protected void begin() {
        if (!initialized) {
            initialized = true;
            solidMask = mapSystem.getMask("solid");
        }
        solidMask.refresh();
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

            if ((physics.vx > 0 && collides(e, px + bounds.maxx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.5f )) ||
                    (physics.vx < 0 && collides(e, px + bounds.minx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.5f))) {
                physics.vx = physics.bounce > 0 ? -physics.vx * physics.bounce : 0;
                px = pos.xy.x;
            }

            if ((physics.vx > 0 && collides(e, px + bounds.maxx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.25f + 3)) ||
                    (physics.vx < 0 && collides(e, px + bounds.minx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.25f + 3))) {
                physics.vx = physics.bounce > 0 ? -physics.vx * physics.bounce : 0;
                px = pos.xy.x;
            }

            if ((physics.vx > 0 && collides(e, px + bounds.maxx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.75f)) ||
                    (physics.vx < 0 && collides(e, px + bounds.minx, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.75f))) {
                physics.vx = physics.bounce > 0 ? -physics.vx * physics.bounce : 0;
                px = pos.xy.x;
            }

            if ((physics.vy > 0 && collides(e, px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.maxy)) ||
                    (physics.vy < 0 && collides(e, px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.miny))) {
                physics.vy = physics.bounce > 0 ? -physics.vy * physics.bounce : 0;
            }

            if ((physics.vy > 0 && collides(e, px + bounds.minx + (bounds.maxx - bounds.minx) * 0.25f, py + bounds.maxy)) ||
                    (physics.vy < 0 && collides(e, px + bounds.minx + (bounds.maxx - bounds.minx) * 0.25f, py + bounds.miny))) {
                physics.vy = physics.bounce > 0 ? -physics.vy * physics.bounce : 0;
            }

            if ((physics.vy > 0 && collides(e, px + bounds.minx + (bounds.maxx - bounds.minx) * 0.75f, py + bounds.maxy)) ||
                    (physics.vy < 0 && collides(e, px + bounds.minx + (bounds.maxx - bounds.minx) * 0.75f, py + bounds.miny))) {
                physics.vy = physics.bounce > 0 ? -physics.vy * physics.bounce : 0;
            }

        }

    }

    public boolean collides(final float x, final float y) {
        if (DEBUG) {
            E.E().pos(x - 2, y - 2).anim("marker").render(5000).terminal();
        }

        return solidMask != null && solidMask.atScreen(x, y, true);
    }

    public boolean collides(final E e, final float x, final float y) {
        if (DEBUG) {
            E.E().pos(x - 2, y - 2).anim("marker").render(5000).terminal();
        }

        if (solidMask != null && solidMask.atScreen(x, y, false)) {
            return true;
        }

        return e.isRobot() && solidForRobotMask(x,y);

    }

    public boolean isLava(final float x, final float y) {
        return deadlyMask != null && deadlyMask.atScreen(x,y, true);
    }

    public boolean canHover(final float x, final float y) {

        if (DEBUG) {
            E.E().pos(x - 2, y - 2).anim("marker").render(5000).tintColor(RED).terminal();
        }

        return canHoverMask != null && canHoverMask.atScreen(x, y, false);
    }


    public boolean solidForRobotMask(final float x, final float y) {

        if (DEBUG) {
            E.E().pos(x - 2, y - 2).anim("marker").render(5000).tintColor(RED).terminal();
        }

        return solidForRobotMask != null && solidForRobotMask.atScreen(x, y, false);
    }
}
