package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Inputs;
import net.mostlyoriginal.game.component.ShipControlled;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class TutorialInputSystem extends FluidIteratingSystem {


    public boolean tutorialMode;
    private E player;
    private boolean pressed;

    public TutorialInputSystem() {
        super(Aspect.all(Inputs.class).exclude(ShipControlled.class));
    }

    @Override
    protected void begin() {
        super.begin();
        player = entityWithTag("player");

        if (player.shipControlledTutorial()) {
            tutorialMode = true;
        }


        if (tutorialMode) {
            if (!pressed) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    G.level = 1;
                    pressed = true;
                    E.E().transitionScreen(GameScreen.class);
                }
            }

            player.inputsLeft(false);
            player.inputsJustLeft(false);
            player.inputsRight(false);
            player.inputsUp(false);
            player.inputsDown(false);
            player.inputsJustRight(false);
            player.inputsFire(false);
        }
    }

    @Override
    protected void process(E e) {
        if (player != null && overlaps(e, player)) {
            final Inputs p = player.getInputs();
            final Inputs in = e.getInputs();

            p.down = in.down;
            p.justLeft = in.justLeft;
            p.justRight = in.justRight;
            p.down = in.down;
            p.left = in.left;
            p.right = in.right;
            p.up = in.up || in.fire; // drift demo.ss
            p.fire = in.fire || in.justFire;
        }
    }
}
