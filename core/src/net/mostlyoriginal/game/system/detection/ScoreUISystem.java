package net.mostlyoriginal.game.system.detection;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.TutorialInputSystem;
import net.mostlyoriginal.game.system.map.MapSystem;
import net.mostlyoriginal.game.system.render.CameraFollowSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;

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
    private boolean finished = false;
    private float finishedTime =0;
    private boolean pressed = false;
    private TutorialInputSystem tutorialInputSystem;
    private TransitionSystem transitionSystem;
    private Integer targetLevel = 0;
    private MapSystem mapSystem;

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
        if (!tutorialInputSystem.tutorialMode && !finished) {
            boolean newHighscore = saveScore();

            final String decimalScore = getDecimalFormattedString("" + score);
            E.E()
                    .labelText((newHighscore ? "NEW HIGHSCORE! " : "SCORE ") + decimalScore)
                    .labelAlign(Label.Align.RIGHT)
                    .fontFontName("italshuge")
                    .tint(1f, 1f, 0f, 1f)
                    .pos(cameraSystem.camera.position.x, cameraSystem.camera.position.y+16)
                    .renderLayer(G.LAYER_PLAYER + 100);

            E.E()
                    .labelText(scoreAsText(score))
                    .labelAlign(Label.Align.RIGHT)
                    .fontFontName("ital")
                    .tint(1f, 1f, 0.2f, 1f)
                    .pos(cameraSystem.camera.position.x, cameraSystem.camera.position.y-20)
                    .renderLayer(G.LAYER_PLAYER + 101);

            E.E()
                    .labelText("Play again? Press space")
                    .labelAlign(Label.Align.RIGHT)
                    .fontFontName("ital")
                    .tint(1f, 1f, 1f, 0f)
                    .pos(cameraSystem.camera.position.x, cameraSystem.camera.position.y - 40)
                    .script(sequence(
                            delay(milliseconds(250)),
                            JamOperationFactory.tintBetween(Tint.TRANSPARENT, Tint.WHITE, milliseconds(100))
                    ))
                    .renderLayer(G.LAYER_PLAYER + 102);
        }
        finished = true;
    }

    private String scoreAsText(int score) {
        if ( score > 1000000 ) return "TUGGIONAIRE";
        if ( score > 500000 ) return "Wait, how, where!?";
        if ( score > 400000 ) return "Ok this isn't possible is it?";
        if ( score > 300000 ) return "Tugotron 5000";
        if ( score > 200000 ) return "Lord of the Track";
        if ( score > 150000 ) return "Bested the developers!";
        if ( score > 100000 ) return "Tow King";
        if ( score >  90000 ) return "Lord of the Pylons";
        if ( score >  80000 ) return "Burning Rubber";
        if ( score >  60000 ) return "Oil Slick";
        if ( score >  40000 ) return "Abandoned Shopping Cart";
        if ( score >  20000 ) return "Rally Granny";
        if ( score >  10000 ) return "Sunday Driver";
        if ( score >   1000 ) return "Parking Brake";
        return "Didn't even try";
    }

    private boolean saveScore() {
        Preferences prefs = Gdx.app.getPreferences("ld41wrecklessR2");
        final String key = "highscore_" + mapSystem.activeLevel;
        if (!prefs.contains(key) || prefs.getInteger(key, score) < score) {
            prefs.putInteger(key, score);
            prefs.flush();
            return true;
        }
        return false;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !finished) {
            displayScorecard();
        }

        if (finished && !pressed) {
            finishedTime += world.delta;

            if (finishedTime > 1f && (tutorialInputSystem.tutorialMode || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.E))) {
                pressed = true;
                G.level = targetLevel;
                transitionSystem.transition(GameScreen.class, 0.1f);
            }
        }
    }

    public void targetLevel(Integer level) {
        targetLevel = level;
        if (level == 2 || level == 3) {
            pressed = true;
            finished = true;
            G.level = targetLevel;
            transitionSystem.transition(GameScreen.class, 0.1f);
        }
    }
}
