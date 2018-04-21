package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class GridSnapSystem extends FluidIteratingSystem {

    private static final int MAX_LANE = 11;
    private static final int MIN_LANE = 1;

    public GridSnapSystem() {
        super(Aspect.all(SnapToGrid.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        if (world.delta == 0) return;

        float maxSpeedX = e.snapToGridPixelsPerSecondX() * world.delta;
        float maxSpeedY = e.snapToGridPixelsPerSecondY() * world.delta;


        float speedX = MathUtils.clamp((e.snapToGridX() * G.CELL_SIZE) - e.posX(), -maxSpeedX, maxSpeedX);
        float speedY = MathUtils.clamp((e.snapToGridY() * G.CELL_SIZE) - e.posY(), -maxSpeedY, maxSpeedY);

        if (e.hasTowed()) {
            e.posX(E(e.towedEntityId()).posX() - G.CELL_SIZE);
        } else {
            e.posX(e.posX() + speedX);
        }
        e.posY(e.posY() + speedY);

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
        final float cX = e.posX() + e.boundsCx();
        return (int) (cX - (cX % G.CELL_SIZE)) / G.CELL_SIZE;
    }

    public static int gridY(E e) {
        final float cY = e.posY() + e.boundsCy();
        return (int) (cY - (cY % G.CELL_SIZE)) / G.CELL_SIZE;
    }

}
