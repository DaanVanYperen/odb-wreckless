package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;

/**
 * @author Daan van Yperen
 */
public class DialogSystem extends FluidIteratingSystem {

    public static final int DIALOG_PADDING_X = 16;
    public static final int DIALOG_PADDING_Y = 16;
    private E player;
    private EntitySpawnerSystem entitySpawnerSystem;
    private GroupManager groupManager;

    private DialogData activeDialog;
    private int activeLine = 0;
    private E dialog;
    private E portrait;
    private CameraSystem cameraSystem;
    private E pressSpace;
    private String align;


    public DialogSystem() {
        super(Aspect.all(Pos.class, Dialog.class));
    }

    @Override
    protected void begin() {
        super.begin();
        player = entityWithTag("player");


        if (activeDialog != null) {
            world.delta=0;
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                activeLine++;
                hideActiveLines();
                if (activeLine >= activeDialog.lines.length) {
                    activeDialog = null;
                    dialog = portrait = pressSpace= null;
                } else {
                    renderActiveLine();
                }
            }
        }
    }

    @Override
    protected void end() {
        super.end();
        if (activeDialog != null) {
            if ( "right".equals(align)) {
                portrait.posX(cameraSystem.camera.position.x + G.SCREEN_WIDTH / 2 - DIALOG_PADDING_X - 64);
                portrait.posY(cameraSystem.camera.position.y - G.SCREEN_HEIGHT / 2 + DIALOG_PADDING_Y);
                dialog.posX(cameraSystem.camera.position.x - G.SCREEN_WIDTH / 2 + DIALOG_PADDING_X + 8);
                dialog.posY(cameraSystem.camera.position.y - G.SCREEN_HEIGHT / 2 + DIALOG_PADDING_Y + 32);
                pressSpace.posX(cameraSystem.camera.position.x - G.SCREEN_WIDTH / 2 + DIALOG_PADDING_X  + 8);
                pressSpace.posY(cameraSystem.camera.position.y - G.SCREEN_HEIGHT / 2 + DIALOG_PADDING_Y + 8);
            } else {
                portrait.posX(cameraSystem.camera.position.x - G.SCREEN_WIDTH / 2 + DIALOG_PADDING_X);
                portrait.posY(cameraSystem.camera.position.y - G.SCREEN_HEIGHT / 2 + DIALOG_PADDING_Y);
                dialog.posX(cameraSystem.camera.position.x - G.SCREEN_WIDTH / 2 + DIALOG_PADDING_X + 64 + 8);
                dialog.posY(cameraSystem.camera.position.y - G.SCREEN_HEIGHT / 2 + DIALOG_PADDING_Y + 32);
                pressSpace.posX(cameraSystem.camera.position.x - G.SCREEN_WIDTH / 2 + DIALOG_PADDING_X + 64 + 8);
                pressSpace.posY(cameraSystem.camera.position.y - G.SCREEN_HEIGHT / 2 + DIALOG_PADDING_Y + 8);
            }
        }
    }

    private void renderActiveLine() {
        LineData line = activeDialog.lines[activeLine];
        align = line.align;
        renderLine(0, line.text, line.portrait);
    }

    private void hideActiveLines() {
        for (Entity dialog : groupManager.getEntities("dialog")) {
            dialog.deleteFromWorld();
        }
    }

    @Override
    protected void process(E e) {
        if (overlaps(e, player)) {
            if (!e.getDialog().data.triggered) {
                e.getDialog().data.triggered = true;
                activeDialog = e.getDialog().data;
                activeLine=0;
                renderActiveLine();
            }
            e.deleteFromWorld();
        }
    }

    private void renderLine(int offsetY, String text, String portrait) {
        int id = entityWithTag("camera").id();
        dialog = E.E()
                .labelText(text)
                .group("dialog")
                .fontFontName("5x5")
                .fontScale(3)
                .pos()
                .renderLayer(2000);
        pressSpace = E.E()
                .labelText("press space")
                .group("dialog")
                .fontFontName("5x5")
                .fontScale(3)
                .pos()
                .renderLayer(2000);
        this.portrait = E.E()
                .anim(portrait)
                .group("dialog")
                .pos()
                .renderLayer(2000);
    }
}
