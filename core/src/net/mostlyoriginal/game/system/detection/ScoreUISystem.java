package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Dialog;
import net.mostlyoriginal.game.component.DialogData;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.LineData;
import net.mostlyoriginal.game.system.CarControlSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class ScoreUISystem extends BaseSystem {

    protected CameraSystem cameraSystem;
    private E eScore;

    @Override
    protected void initialize() {
        super.initialize();
        eScore = E.E()
                .labelText("1.000.000.000")
                .fontFontName("ital")
                .tint(0f, 0f, 1f, 1f)
                .pos(0, G.SCREEN_HEIGHT / 2f)
                .renderLayer(G.LAYER_PLAYER + 100);
    }

    @Override
    protected void processSystem() {
        eScore.posX((int)cameraSystem.camera.position.x);
    }
}
