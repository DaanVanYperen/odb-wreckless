package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.ChainingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class GridSnapSystem extends FluidIteratingSystem {

    private static final int MAX_LANE = 11;
    private static final int MIN_LANE = 1;
    private ChainingSystem chainingSystem;

    public GridSnapSystem() {

        super(Aspect.all(SnapToGrid.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    protected void process(E e) {
        if (world.delta == 0) return;

        float maxSpeedX = e.snapToGridPixelsPerSecondX();
        float maxSpeedY = e.snapToGridPixelsPerSecondY();


        final int sx = e.snapToGridX();
        final int sy = e.snapToGridY();

        float xDestination = 0;
        float yDestination = 0;

        boolean towedDrifting =false;
        if ( e.hasTowed() && e.towedDrifting() ) {
            // allow drifting cars to catch up.
            maxSpeedX *= 2;
            maxSpeedY *= 2;
            e.towedDrifting(false);
            towedDrifting=true;
        }


        boolean drifting = e.hasShipControlled() && e.shipControlledReleasing();

        if ( drifting ) maxSpeedX *= 0.5f;

        float speedX = MathUtils.clamp(((sx * G.CELL_SIZE) - e.posX()) * maxSpeedX * 0.1f, -maxSpeedX, maxSpeedX);
        float speedY = MathUtils.clamp(((sy * G.CELL_SIZE) - e.posY()) * maxSpeedX * 0.1f, -maxSpeedY, maxSpeedY);

        final boolean isOnDesiredGrid = gridX(e) == sx && gridY(e) == sy;

        if (e.hasTowed() && e.towedEntityId() != -1) {
            e.posX(E(e.towedEntityId()).posX() - (towedDrifting ? 0 : G.CELL_SIZE));
        } else {

            if (isOnDesiredGrid) {
                if ( e.hasShipControlled() ) {
                    xDestination = e.posX() + maxSpeedX * 0.3f * world.delta;
                    e.snapToGridX(gridX(e)); // assume our current location is our desired location.
                    e.posX(xDestination);
                }
            } else {
                xDestination = e.posX() + speedX * world.delta;
                e.posX(xDestination);
            }

        }

        yDestination = e.posY() + speedY * world.delta;

        e.posY(yDestination);

        final int maxAngle = drifting || towedDrifting ? 80 : 20;

        e.angleRotation(MathUtils.clamp(towedDrifting ? speedY * maxAngle : speedY,  -maxAngle, maxAngle));


        // ideal vector.
//        v.set(e.snapToGridX() * G.CELL_SIZE, e.snapToGridY() * G.CELL_SIZE).sub(e.posX(), e.posY());
//        v.scl(world.delta).clamp(0, (PIXELS_PER_SECOND*PIXELS_PER_SECOND) * world.delta);
//
//        e.posX(e.posX() + v.x);
//        e.posY(e.posY() + v.y);
    }

    public void moveRelativeToSelf(E e, int dx, int dy) {
        moveRelativeToOther(e, e, dx, dy);
    }

    public void moveRelativeToOther(E e, E other, int dx, int dy) {
        e.snapToGridX(MathUtils.clamp(gridX(other) + dx, 0, 9999));
        e.snapToGridY(MathUtils.clamp(gridY(other) + dy, MIN_LANE, MAX_LANE));
    }

    public static int gridX(E e) {
        if (e == null) return 0;
        final float cX = e.posX() + e.boundsCx();
        return (int) (cX - (cX % G.CELL_SIZE)) / G.CELL_SIZE;
    }

    public static int gridY(E e) {
        if (e == null) return 0;
        final float cY = e.posY() + e.boundsCy();
        return (int) (cY - (cY % G.CELL_SIZE)) / G.CELL_SIZE;
    }

    public void instaSnap(E eCar) {
        eCar.posX(gridX(eCar) * G.CELL_SIZE);
        eCar.posY(gridY(eCar) * G.CELL_SIZE);
    }
}
