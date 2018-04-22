package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.g3d.utils.ShapeCache;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.component.Cashable;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.SnapToGrid;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;
import static net.mostlyoriginal.api.component.graphics.Tint.WHITE;
import static net.mostlyoriginal.api.operation.OperationFactory.*;
import static net.mostlyoriginal.api.utils.Duration.milliseconds;

/**
 * @author Daan van Yperen
 */
public class RewardSystem extends FluidIteratingSystem {

    private static final float FADEOUT_DURATION = 1f;
    private static final float GLOW_MAX_SCALE = 2f;

    public RewardSystem() {
        super(Aspect.all(Cashable.class));
    }

    private TowedSystem towedSystem;

    @Override
    protected void process(E shackle) {
        sfx(shackle);
        killTows(shackle);
        shackle.removeCashable();
        shackle.script(deleteFromWorld());
    }

    private void sfx(E shackle) {
        final float x = GridSnapSystem.gridX(shackle) * G.CELL_SIZE;
        final float y = GridSnapSystem.gridY(shackle) * G.CELL_SIZE;
        final float compensateForScaling = ((G.CELL_SIZE * GLOW_MAX_SCALE)-G.CELL_SIZE) * 0.5f;

        E.E()
                .posX(x)
                .posY(y)
                .renderLayer(G.LAYER_PARTICLES)
                .anim(shackle.animId() + "-score")
                .script(sequence(
                        delay(milliseconds(250)),
                        deleteFromWorld()
                ));
    }

    private void killTows(E shackle) {
        if (shackle.hasTowing()) {
            // currenly cargo loaded.
            towedSystem.disconnectCargoFrom(shackle, true);
        }
        if (shackle.hasTowed()) {
            towedSystem.disconnectFromTowingCar(shackle, true);
        }
    }

}
