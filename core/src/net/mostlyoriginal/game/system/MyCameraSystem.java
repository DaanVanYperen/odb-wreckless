package net.mostlyoriginal.game.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;

/**
 * @author Daan van Yperen
 */
public class MyCameraSystem extends CameraSystem {

    public static final float INTO_FOCUS_COOLDOWN = 10f;
    float cooldown = INTO_FOCUS_COOLDOWN;
    float y = (G.SCREEN_HEIGHT / G.CAMERA_ZOOM) / 2;

    StarEffectSystem starEffectSystem;

    public MyCameraSystem(int cameraZoom) {
        super(cameraZoom);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void processSystem() {
        if (cooldown > 0 && !G.DEBUG_SKIP_INTRO) {
            if ( cooldown < 2f ) {
                starEffectSystem.active=false;
            }
            starEffectSystem.active = true;
            cooldown -= world.delta;
            if (cooldown < 0) cooldown = 0;
            camera.position.y = Interpolation.pow2In.apply(y, y + G.SCREEN_HEIGHT / 2, cooldown / INTO_FOCUS_COOLDOWN);
            camera.update();
        } else starEffectSystem.active = false;
        super.processSystem();
    }
}
