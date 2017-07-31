package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.render.CameraFocus;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.MapSystem;

/**
 * @author Daan van Yperen
 */
public class CameraClampToMapSystem extends BaseSystem {

    private CameraSystem cameraSystem;
    private MapSystem mapSystem;

    @Override
    protected void processSystem() {


        float halfScreenWidth = (Gdx.graphics.getWidth() / G.CAMERA_ZOOM) * 0.5f;
        float halfScreenHeight = (Gdx.graphics.getHeight() / G.CAMERA_ZOOM) * 0.5f;

        float minCameraX = halfScreenWidth;
        float maxCameraX = mapSystem.width * G.CELL_SIZE - halfScreenWidth;
        float minCameraY = halfScreenHeight;
        float maxCameraY = mapSystem.height * G.CELL_SIZE - halfScreenHeight;

        if (minCameraX > maxCameraX) {
            maxCameraX = minCameraX;
        }
        if (minCameraY > maxCameraY) maxCameraY = minCameraY;

        cameraSystem.camera.position.x = MathUtils.clamp(cameraSystem.camera.position.x, minCameraX, maxCameraX);
        cameraSystem.camera.position.y = MathUtils.clamp(cameraSystem.camera.position.y, minCameraY, maxCameraY);
        cameraSystem.camera.update();
    }
}

