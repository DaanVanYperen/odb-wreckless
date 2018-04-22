package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Dialog;
import net.mostlyoriginal.game.component.DialogData;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.LineData;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.CarControlSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.render.CameraFollowSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import java.util.StringTokenizer;

import static net.mostlyoriginal.api.operation.OperationFactory.*;
import static net.mostlyoriginal.api.utils.Duration.milliseconds;

/**
 * @author Daan van Yperen
 */
public class ScoreUISystem extends BaseSystem {

    protected CameraSystem cameraSystem;
    private E eScore;
    private CameraFollowSystem cameraFollowSystem;


    private int score = 0;
    private boolean finished;
    private float finishedTime;

    @Override
    protected void initialize() {
        super.initialize();
        eScore = E.E()
                .labelText("0")
                .fontFontName("ital")
                .tint(1f, 1f, 1f, 1f)
                .pos(0, G.SCREEN_HEIGHT / 2f)
                .renderLayer(G.LAYER_PLAYER + 100);
    }

    public void displayScorecard() {
        E.E()
                .labelText("FINAL SCORE " + getDecimalFormattedString("" + score))
                .labelAlign(Label.Align.RIGHT)
                .fontFontName("italshuge")
                .tint(1f, 1f, 0f, 1f)
                .pos(cameraSystem.camera.position.x, cameraSystem.camera.position.y)
                .renderLayer(G.LAYER_PLAYER + 100);
        E.E()
                .labelText("Play again? Press space")
                .labelAlign(Label.Align.RIGHT)
                .fontFontName("ital")
                .tint(1f, 1f, 1f, 0f)
                .pos(cameraSystem.camera.position.x, cameraSystem.camera.position.y - 40)
                .script(sequence(
                        delay(milliseconds(250)),
                        JamOperationFactory.tintTo(Tint.WHITE)
                ))
                .renderLayer(G.LAYER_PLAYER + 100);
        finished = true;
    }

    public void addPoints(int points) {
        score += points;
        eScore.labelText(getDecimalFormattedString("" + score));
    }


    public static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    @Override
    protected void processSystem() {
        eScore.posX(cameraSystem.camera.position.x - (G.SCREEN_WIDTH / G.CAMERA_ZOOM) / 2);
        eScore.posY(cameraSystem.camera.position.y + (G.SCREEN_HEIGHT / G.CAMERA_ZOOM) / 2 - 10);

        if (finished) {
            finishedTime += world.delta;

            if (finishedTime > 1f && (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.E))) {
                E.E().transitionScreen(GameScreen.class);

            }
        }
    }
}
