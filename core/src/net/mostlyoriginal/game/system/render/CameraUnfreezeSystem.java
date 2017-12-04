package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * Unfreeze near camera.
 *
 * @author Daan van Yperen
 */
public class CameraUnfreezeSystem extends FluidIteratingSystem {

    CameraSystem cameraSystem;
    private MyAnimRenderSystem myAnimRenderSystem;
    private boolean lockCamera;
    private CameraFollowSystem cameraFollowSystem;
    private float maxY;

    public CameraUnfreezeSystem() {
        super(Aspect.all(Pos.class, Frozen.class));
    }

    @Override
    protected void begin() {
        super.begin();
        final E camera = entityWithTag("camera");
        maxY = cameraFollowSystem.minCameraY() + (G.SCREEN_HEIGHT / G.CAMERA_ZOOM);
    }

    @Override
    protected void process(E e) {
        if ( e.posY() <= maxY - G.SCREEN_HEIGHT ) {
            e.removeFrozen();
        }
    }
}

