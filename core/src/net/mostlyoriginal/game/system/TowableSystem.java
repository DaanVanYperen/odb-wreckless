package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class TowableSystem extends FluidIteratingSystem {

    private TowedSystem towedSystem;
    private E player;

    public TowableSystem() {
        super(Aspect.all(Towable.class, SnapToGrid.class).exclude(Towed.class, Towing.class));
    }


    @Override
    protected void begin() {
        super.begin();
        player = entityWithTag("player");
    }


    @Override
    protected void process(E e) {
        if ( overlaps(e, player)) {
            towedSystem.hookOnto(player, e);
            G.sfx.play("hook");
        }
    }

    private E getTowing(E e) {
        return e.hasTowing() ? E(e.towingEntityId()) : null;
    }

}
