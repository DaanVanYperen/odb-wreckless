package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.system.camera.CameraShakeSystem;
import net.mostlyoriginal.game.component.Exit;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.FollowSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class ExitSystem extends FluidIteratingSystem {

    public TransitionSystem transitionSystem;
    private FollowSystem followSystem;
    private MyAnimRenderSystem animSystem;
    private GameScreenAssetSystem assetSystem;
    private DialogSystem dialogSystem;
    private CameraShakeSystem cameraShakeSystem;

    public ExitSystem() {
        super(Aspect.all(Exit.class, Pos.class));
    }

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        E robot = entityWithTag("robot");
        if (overlaps(robot, e) && !e.exitOpen()) {
            if (robot.chargeCharge() < G.BARS_NEEDED_FOR_BREAKING_DOOR && e.exitCooldown() >= 2.9f) {
                if ( !robot.hasNeedsBatteries() ) {
                    robot.needsBatteries();
                    dialogSystem.robotSay(DialogSystem.Dialog.BATTERY, 0.5f,5f);
                }
            } else {
                robot.removeNeedsBatteries();
                e.exitCooldown(e.exitCooldown() - world.delta);
                if (!robot.animId().equals("robot-fight-stand")) {
                    animSystem.forceAnim(robot, "robot-fight-stand");
                }
                if (e.exitCooldown() < 2 && !e.exitBroken()) {
                    e.exitBroken(true);
                    assetSystem.playSfx("door_break");
                    followSystem.expendCharge(e, G.BARS_NEEDED_FOR_BREAKING_DOOR / 2f);
                    E.E().posX(e.posX() - 16).posY(e.posY()).animId("exit-damaged").render(G.LAYER_DOOR);
                    cameraShakeSystem.shake(10);
                }
                if (e.exitCooldown() < 1 && !e.exitOpen()) {
                    assetSystem.playSfx("door_break");
                    e.exitOpen(true);
                    followSystem.expendCharge(e, G.BARS_NEEDED_FOR_BREAKING_DOOR / 2f);
                    E.E().posX(e.posX() - 16).posY(e.posY()).animId("exit-open").render(G.LAYER_DOOR + 1);
                    cameraShakeSystem.shake(10);
                }
                cameraShakeSystem.shake(4);
            }
        } else robot.removeNeedsBatteries();

        if (e.exitOpen() && overlaps(player, e) && overlaps(robot, e)) {
            doExit(e);
        }

        if (!e.exitOpen() && overlaps(player, e) && !overlaps(robot, e) && !robot.hasSlumbering()) {
            dialogSystem.playerSay(DialogSystem.Dialog.E, 0f, 1f);
        }
    }

    private void doExit(E e) {
        e.removeExit();
        G.level++;
        transitionSystem.transition(GameScreen.class, 0.1f);
    }
}
