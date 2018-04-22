package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class DriftSystem extends FluidIteratingSystem {

    TowedSystem towedSystem;
    GridSnapSystem gridSnapSystem;

    public DriftSystem() {
        super(Aspect.all(Towing.class, ShipControlled.class, Drifting.class));
    }


    @Override
    protected void process(E e) {

        int startX = GridSnapSystem.gridX(e);
        int startY = GridSnapSystem.gridY(e);

        E subject = towedSystem.getCargo(e);
        while ( subject != null ) {
            //startX += e.driftingDx();
            startY -= e.driftingDy();

            subject.snapToGridX(startX);
            subject.snapToGridY(startY);
            subject.towedDrifting(true);

            subject = towedSystem.getCargo(subject);
        }
    }
}
