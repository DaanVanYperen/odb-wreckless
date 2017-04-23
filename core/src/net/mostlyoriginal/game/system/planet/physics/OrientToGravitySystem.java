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
import net.mostlyoriginal.game.component.PlanetCoord;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class OrientToGravitySystem extends FluidIteratingSystem {

    private TagManager tagManager;

    public OrientToGravitySystem() {
        super(Aspect.all(Pos.class, OrientToGravity.class, PlanetCoord.class, Angle.class));
    }

    private Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        PlanetCell.CellType type = e.planetCoordCell().type;
        if (type.density == null || type.density >= 1f ) {

            e.angleRotation(90 + v.set(G.PLANET_CENTER_X, G.PLANET_CENTER_Y).sub(e.posX(), e.posY()).angle()).physicsVr(0);
        }
    }
}
