package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.render.CameraFocus;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class CameraFollowSystem extends FluidIteratingSystem {

    CameraSystem cameraSystem;
    private MyAnimRenderSystem myAnimRenderSystem;

    public CameraFollowSystem() {
        super(Aspect.all(Pos.class, CameraFocus.class));
    }

    @Override
    protected void process(E e) {
        cameraSystem.camera.position.x = myAnimRenderSystem.roundToPixels(e.posX());
        cameraSystem.camera.position.y = myAnimRenderSystem.roundToPixels(e.posY());
        cameraSystem.camera.update();
    }
}

