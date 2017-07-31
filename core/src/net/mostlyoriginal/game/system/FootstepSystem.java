package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraShakeSystem;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.ParticleSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class FootstepSystem extends FluidIteratingSystem {

    private GameScreenAssetSystem assetSystem;
    private CameraShakeSystem cameraShakeSystem;

    public FootstepSystem() {
        super(Aspect.all(Footsteps.class, Pos.class));
    }

    ParticleSystem particleSystem;

    @Override
    protected void process(E e) {
        if (e.wallSensorOnPlatform() || e.wallSensorOnFloor()) {
            float testX = e.posX() + e.boundsMinx() + (e.boundsMaxx() - e.boundsMinx()) * 0.5f;
            if (Math.abs(testX - e.footstepsLastX()) > e.footstepsStepSize()) {
                particleSystem.dust(testX + (testX - e.footstepsLastX() > 0 ? -4 : 4), e.posY(), 90 + (testX - e.footstepsLastX() > 0 ? 45 : -45));
                e.footstepsLastX(testX);
                e.footstepsSfxCount(e.footstepsSfxCount() + 1);
                if (e.footstepsSfxCount() >= 3 && e.footstepsSfx() != null) {
                    e.footstepsSfxCount(0);
                    assetSystem.playSfx(e.footstepsSfx());

                    if ( e.isRobot()) {
                        cameraShakeSystem.shake(1);
                        particleSystem.sprinkleSand(15);
                    }
                }
            }
        }
    }
}
