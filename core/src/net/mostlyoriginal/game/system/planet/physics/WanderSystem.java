package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.OrientToGravity;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.Wander;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class WanderSystem extends FluidIteratingSystem {

    private TagManager tagManager;
    PlanetCoordSystem planetCoordSystem;

    public WanderSystem() {
        super(Aspect.all(Pos.class, Wander.class));
    }

    private Vector2 v = new Vector2();
    Pos tmpPos = new Pos();

    @Override
    protected void process(E e) {
        v.set(10f, 0).rotate(
                e.wanderDirection() == Wander.Direction.LEFT ?
                        e.angleRotation() - 180f - 45f
                        : e.angleRotation() + 45f).scl(world.delta);

        // save pos for later.
        tmpPos.set(e.getPos());

        // move and see what happens.
        Pos pos = e.getPos();
        pos.xy.x += v.x;
        pos.xy.y += v.y;

        planetCoordSystem.updateCoord(e);
        if (!canSurvive(e)) {
            e.pos(tmpPos);
        }

        if (e.hasDolphinized()) {
            e.anim("dolphin").removeAngry();
        } else if (e.hasAngry()) {
            e.anim("angrydude");
        } else {
            e.anim("dude");
        }

        if (!canSurvive(e)) {
            e.died();
        }
    }

    private boolean canSurvive(E e) {
        PlanetCell cell = e.planetboundCell();
        if (cell != null) {
            if (e.hasDolphinized() && !((cell.type == null) || (cell.type == PlanetCell.CellType.AIR) || (cell.type == PlanetCell.CellType.CLOUD) || (cell.type == PlanetCell.CellType.STEAM) || (cell.type == PlanetCell.CellType.WATER))) {
                return false;
            }

            if (cell.type == PlanetCell.CellType.LAVA || cell.type == PlanetCell.CellType.LAVA_CRUST) {
                return false;
            }
        }
        return true;
    }
}
