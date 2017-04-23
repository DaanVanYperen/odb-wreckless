package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.OrientToGravity;
import net.mostlyoriginal.game.component.Wander;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class WanderSystem extends FluidIteratingSystem {

    private TagManager tagManager;

    public WanderSystem() {
        super(Aspect.all(Pos.class, Wander.class));
    }

    private Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        v.set(10f, 0).rotate(
                e.wanderDirection() == Wander.Direction.LEFT ?
                        e.angleRotation() - 180f - 45f
                        : e.angleRotation() + 45f).scl(world.delta);

        Pos pos = e.getPos();
        pos.xy.x += v.x;
        pos.xy.y += v.y;

        if (e.hasAngry()) {
            e.anim("angrydude");
        } else {
            e.anim("dude");

        }
    }
}
