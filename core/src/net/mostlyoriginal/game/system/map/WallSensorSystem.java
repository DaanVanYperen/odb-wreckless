package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class WallSensorSystem extends FluidIteratingSystem {

    private MapSystem mapSystem;

    private boolean initialized;
    private MapMask solidMask;

    public WallSensorSystem() {
        super(Aspect.all(WallSensor.class, Pos.class, Bounds.class));
    }

    @Override
    protected void begin() {
        if (!initialized) {
            initialized = true;
            solidMask = mapSystem.getMask("solid");
        }
    }

    @Override
    protected void process(E e) {

        float px = e.posX();
        float py = e.posY();

        final Bounds bounds = e.getBounds();

        final boolean onFloor = collides(px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.miny - 1);
        final boolean onCeiling = collides(px + bounds.minx + (bounds.maxx - bounds.minx) * 0.5f, py + bounds.maxy + 1);
        final boolean onEastWall = collides(px + bounds.maxx + 1, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.5f);
        final boolean onWestWall = collides(px + bounds.minx - 1, py + bounds.miny + (bounds.maxy - bounds.miny) * 0.5f);

        e
                .wallSensorOnVerticalSurface(onEastWall || onWestWall)
                .wallSensorOnFloor(onFloor)
                .wallSensorOnHorizontalSurface(onCeiling || onFloor)
                .mapWallSensorWallAngle(onFloor ? 90 :
                        onCeiling ? -90 :
                                onEastWall ? 0 :
                                        onWestWall ? 180 : 90);
    }

    private boolean collides(final float x, final float y) {
        return solidMask.atScreen(x, y, true);
    }
}