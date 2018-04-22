package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.GridSnapSystem;
import net.mostlyoriginal.game.system.TowedSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;

/**
 * @author Daan van Yperen
 */
public class SpinoutSystem extends FluidIteratingSystem {

    private TowedSystem towedSystem;
    private EntitySpawnerSystem entitySpawnerSystem;

    public SpinoutSystem() {
        super(Aspect.all(Spinout.class));
    }

    Vector2 v2 = new Vector2();

    @Override
    protected void process(E e) {

        e.spinoutFactor(e.spinoutFactor() + world.delta * 0.5f);
        if (e.spinoutFactor() < 1) {
            final float a = 1 - Interpolation.linear.apply(e.spinoutFactor());

            v2.set( 0,world.delta * 50 * a).rotate(e.spinoutDirection());

            e.posX(e.posX() + v2.x);
            e.posY(e.posY() + v2.y);

            e.angleRotate(world.delta * (e.spinoutAngle()) * a);

            if (e.hasTowing()) {
                towedSystem.disconnectCargoFrom(e, true);
            }
        } else {
            e.removeSpinout();
            if (e.hasChainable()) {
                final E e1 = entitySpawnerSystem.assembleCar((int) e.posX(), (int) e.posY(), e.chainableColor().name());
                e1.angleRotation(e.angleRotation());
                e.script(OperationFactory.deleteFromWorld());
            }
        }
    }

}
