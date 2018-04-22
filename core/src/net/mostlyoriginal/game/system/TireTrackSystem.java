package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.TireTrack;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.deleteFromWorld;
import static net.mostlyoriginal.api.operation.OperationFactory.sequence;
import static net.mostlyoriginal.api.utils.Duration.milliseconds;

/**
 * @author Daan van Yperen
 */
public class TireTrackSystem extends FluidIteratingSystem {

    private EBag onGrid;

    public TireTrackSystem() {
        super(Aspect.all(TireTrack.class, Pos.class));
    }

    private Tint LIGHT_TRACKS = new Tint(1f,1f,1f,0.25f);
    private Tint DARK_TRACKS = new Tint(1f,1f,1f,0.75f);
    private Tint VERY_LIGHT_TRACKS = new Tint(1f,1f,1f,0.10f);

    @Override
    protected void process(E e) {

        e.tireTrackCooldown(e.tireTrackCooldown() - world.delta);
        if (e.tireTrackCooldown() <= 0) {
            e.tireTrackCooldown(0.025f);
            E.E()
                    .pos(e.posX(), e.posY())
                    .renderLayer(G.LAYER_GREMLIN - 10)
                    .bounds(0, 0, G.CELL_SIZE, G.CELL_SIZE)
                    .tint(1f,1f,1f,1f)
                    .angleRotation(e.angleRotation())
                    .anim("player-skidmarks").script(sequence(
                    JamOperationFactory.tintBetween(e.hasDrifting() || e.hasSpinout() ? DARK_TRACKS : e.hasShipControlled() ? VERY_LIGHT_TRACKS : LIGHT_TRACKS, Tint.TRANSPARENT,

                            milliseconds(e.hasSpinout() || e.hasDrifting() ? 10000 : 1000)),
                    deleteFromWorld()
            ));
        }
    }
}
