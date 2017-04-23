package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import javafx.scene.paint.Color;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class FlammableSystem extends FluidIteratingSystem {

    private TagManager tagManager;

    public FlammableSystem() {
        super(Aspect.all(PlanetCoord.class, Flammable.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        final PlanetCell cell = e.planetCoordCell();
        if (cell != null && (cell.type == PlanetCell.CellType.LAVA || cell.type == PlanetCell.CellType.LAVA_CRUST)) {
            E.E()
                    .pos(e.getPos())
                    .anim("dudeghost")
                    .renderLayer(G.LAYER_GHOST)
                    .orientToGravity()
                    .angle()
                    .tint(Tint.WHITE)
                    .physicsVelocity(e.planetCoordGravity().x * -100f, e.planetCoordGravity().y * -100f, 0f)
                    .script(
                                    sequence(delay(0.5f), tween(Tint.WHITE, Tint.TRANSPARENT, 1f), deleteFromWorld()));

            e.deleteFromWorld();

        }
    }
}
