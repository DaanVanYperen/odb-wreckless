package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.operation.flow.SequenceOperation;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Farewell;
import net.mostlyoriginal.game.component.Trigger;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class FarewellSystem extends FluidIteratingSystem {

    private GameScreenAssetSystem assetSystem;
    private float age = 0;
    private DialogSystem dialogSystem;
    private boolean step1 = false;
    private boolean step2 = false;
    private boolean step3 = false;
    private boolean step4 = false;
    private boolean step5 = false;
    private boolean step6 = false;
    private boolean step7 = false;
    private boolean step8 = false;
    private boolean step9 = false;
    private float STEP = 3f;
    private boolean step0 = false;
    private MyAnimRenderSystem animSystem;
    private boolean step1point5 = false;
    private CameraSystem cameraSystem;

    public FarewellSystem() {

        super(Aspect.all(Farewell.class));
    }

    @Override
    protected void process(E e) {
        final E player = entityWithTag("player");
        final E robot = entityWithTag("robot");
        age += world.delta;


        if (!step1point5 && age > STEP * 0.5f) {
            step1point5 = true;
            player.animFlippedX(true);
        }

        if (!step1 && age > STEP) {
            step1 = true;
            robot.chargeCharge(3f);
            dialogSystem.playerSay(DialogSystem.Dialog.SAD, 0, 2f);
            dialogSystem.robotSay(DialogSystem.Dialog.SAD, 0, 4f);
        }

        if (!step2 && age > STEP * 2) {
            step2 = true;
            dialogSystem.playerSay(DialogSystem.Dialog.QUESTION, 0, 2f);
            robot.chargeCharge(2f);
        }

        if (!step3 && age > STEP * 3) {
            step3 = true;
            dialogSystem.robotSay(DialogSystem.Dialog.COME, 0, 1f);
            dialogSystem.robotSay(DialogSystem.Dialog.S76, 1f, 2f);
            robot.chargeCharge(1.5f);
        }

        if (!step4 && age > STEP * 4) {
            step4 = true;
            dialogSystem.playerSay(DialogSystem.Dialog.COME, 0, 1f);
            dialogSystem.playerSay(DialogSystem.Dialog.FLOWER, 0, 2f);
        }

        if (!step5 && age > STEP * 5) {
            step5 = true;
            // WALK AWAY
            player.removeCameraFocus();
            robot.cameraFocus();
            player.physicsVx(50);
            player.physicsVy(0);
            player.anim("player-walk");
            player.animLoop(true);
            player.animFlippedX(false);
            player.physicsFriction(0);
            player.script(tween(Tint.WHITE, Tint.TRANSPARENT, 1.5f));
            animSystem.forceAnim(robot, "robot-wave");
        }

        if (!step6 && age > STEP * 7) {
            step6 = true;
            robot.chargeCharge(1f);
            dialogSystem.robotSay(DialogSystem.Dialog.FLOWER, 0, 8000f);
        }

        if (!step7 && age > STEP * 9) {
            step7 = true;
            robot.chargeCharge(0f).slumbering();
        }

        if (!step8 && age > STEP * 10.5) {
            step8 = true;
            E.E()
                    .pos(robot.posX() - 160, robot.posY() + robot.boundsCx() - 6)
                    .anim("logo")
                    .bounds(0, 0, 1, 1)
                    .tint(Tint.TRANSPARENT)
                    .render(100000)
                    .script(tween(Tint.TRANSPARENT, Tint.WHITE, 3f));
        }
    }

    public void start() {
        if (!step0) {
            step0 = true;
            final E player = entityWithTag("player");
            final E robot = entityWithTag("robot");
            player.removeMortal().removePlayerControlled().anim("player-idle");
            E marker = entityWithTag("marker");
            marker.deleteFromWorld();
            robot.anim("robot-idle");
            dialogSystem.playerSay(DialogSystem.Dialog.HAPPY, 0, 2f);
            E.E().farewell();
        }
    }
}
