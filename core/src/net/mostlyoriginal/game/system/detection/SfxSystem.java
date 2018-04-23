package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Sfx;
import net.mostlyoriginal.game.component.Spinout;
import net.mostlyoriginal.game.system.TowedSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;

/**
 * @author Daan van Yperen
 */
public class SfxSystem extends FluidIteratingSystem {

    public SfxSystem() {
        super(Aspect.all(Sfx.class));
    }

    Vector2 v2 = new Vector2();

    @Override
    protected void process(E e) {
        e.sfxCooldown(e.sfxCooldown() - world.delta);
        if (e.sfxCooldown() <= 0) {
            G.sfx.play(e.sfxName());
            e.deleteFromWorld();
        }
    }

}
