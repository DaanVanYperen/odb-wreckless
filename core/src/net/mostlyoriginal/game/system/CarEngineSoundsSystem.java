package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class CarEngineSoundsSystem extends FluidIteratingSystem {

    private static final float REV_CHANGE_CYCLE = 0.40f;
    private static final float REV_CYCLE = 0.23f;
    private static final float VOLUME = 0.5f;

    public CarEngineSoundsSystem() {
        super(Aspect.all(ShipControlled.class));
    }

    String nextSfx = "";
    float cooldown = 0;
    boolean oldRev = false;

    @Override
    protected void process(E e) {
        if (world.delta == 0) return;

        final int dx = e.shipControlledDx();
        final int dy = e.shipControlledDy();
        boolean curRev = !(dx == 0 && dy == 0);
        boolean revChange = oldRev != curRev;

        cooldown -= world.delta;
        if (cooldown <= 0 || revChange) {
            cooldown = REV_CYCLE;
            oldRev = curRev;

            if (revChange) {
                cooldown = REV_CHANGE_CYCLE;
                G.sfx.play(curRev ? "truck_revup_fast" : "truck_revdown_fast", VOLUME);
            } else
                G.sfx.play(curRev ? "truck_revhigh" : "truck_revlow", VOLUME);
        }
    }
}
