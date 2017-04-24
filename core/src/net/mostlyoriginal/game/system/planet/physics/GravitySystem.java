package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.game.component.Mass;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.Planetbound;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class GravitySystem extends FluidIteratingSystem {

    private TagManager tagManager;

    public GravitySystem() {
        super(Aspect.all(Pos.class, Planetbound.class, Mass.class, Angle.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        Vector2 gravityVector = v.set(e.planetboundGravity());

        // apply gravity.
        Physics physics = e.getPhysics();

        // don't move through solid matter.
        PlanetCell cell = e.planetboundCell();
        if (cell != null && cell.type != null && (cell.type.density == null || cell.type.density >= 1f)) {
            if (cell.type.flows() && !e.hasDolphinized()) {
                gravityVector.rotate(180);
                physics.vx = 0;
                physics.vy = 0;
            } else {
                gravityVector.set(0, 0);
                physics.vx = 0;
                physics.vy = 0;
            }
        }

        physics.vx += gravityVector.x * 4f;
        physics.vy += gravityVector.y * 4f;
        physics.friction = 0.05f;

    }
}
