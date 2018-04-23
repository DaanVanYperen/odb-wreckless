package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class CarEngineSoundsSystem extends FluidIteratingSystem {

    private static final float REV_CHANGE_CYCLE = 0.60f;
    private static final float REV_CYCLE = 0.14f;

    public CarEngineSoundsSystem() {
        super(Aspect.all(ShipControlled.class));
    }

    String nextSfx = "";
    float cooldown = 0;
    boolean highRev = false;

    @Override
    protected void process(E e) {
        if (world.delta == 0) return;

        cooldown -= world.delta;
        if (cooldown <= 0) {
            final int dx = e.shipControlledDx();
            final int dy = e.shipControlledDx();

            String sfx = "truck_revlow";

            if ( e.hasSpinout() ) {
                if (highRev) {
                    cooldown += REV_CHANGE_CYCLE;
                    G.sfx.play("truck_revdown_fast", 2f);
                }
                else {
                    cooldown += REV_CYCLE;
                    G.sfx.play("truck_revlow", 2f);
                }
                highRev=false;
                return;
            }

            if (highRev) {
                if (dx == 0 && dy == 0) {
                    cooldown += 0;
                    sfx = "truck_revdown_fast";
                    highRev = false;
                } else {
                    cooldown += REV_CYCLE;
                    sfx = "truck_revhigh";
                }
            } else {
                if (dx != 0 || dy != 0) {
                    cooldown += 0;
                    sfx = "truck_revup_fast";
                    highRev = true;
                } else {
                    cooldown += REV_CYCLE;
                    sfx = "truck_revlow";
                }

            }
            G.sfx.play(sfx, 2f);

        }
    }
}
