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
        final float x = shackle.posX();
        final float y = shackle.posY();
        final float compensateForScaling = ((G.CELL_SIZE * GLOW_MAX_SCALE)-G.CELL_SIZE) * 0.5f;

        E.E()
                .posX(x)
                .posY(y)
                .renderLayer(G.LAYER_PARTICLES)
                .glowAnim(shackle.animId())
                .tint(1f, 1f, 1f, 1f)
                .script(sequence(
                        parallel(
                                JamOperationFactory.moveBetween(x, y, x - compensateForScaling, y - compensateForScaling, FADEOUT_DURATION, Interpolation.pow2Out),
                                JamOperationFactory.scaleBetween(1f, GLOW_MAX_SCALE, FADEOUT_DURATION, Interpolation.pow2Out),
                                JamOperationFactory.tintBetween(WHITE, Tint.TRANSPARENT, FADEOUT_DURATION, Interpolation.pow2Out)
                        ),
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
