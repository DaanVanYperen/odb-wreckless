package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.Exit;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Trigger;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class TriggerSystem extends FluidIteratingSystem {

    private GameScreenAssetSystem assetSystem;
    private FarewellSystem farewellSystem;

    public TriggerSystem() {
        super(Aspect.all(Trigger.class, Pos.class));
    }

    String playingSong;

    @Override
    protected void process(E e) {
        E player = entityWithTag("player");
        E robot = entityWithTag("robot");

        boolean robotOverlaps = overlaps(robot, e);
        if (overlaps(player, e) || robotOverlaps) {
            switch (e.triggerTrigger()) {
                case "start-running":
                    player.running();
                    player.removeCameraFocus();
                    E.E().pos(player.getPos()).cameraFocus().physicsFriction(0).physicsVx(50).tag("pacer");
                    robot.running();
                    e.removeTrigger();
                    break;
                case "stop-running":
                    player.removeRunning();
                    player.cameraFocus();
                    robot.removeRunning();
                    E pacer = entityWithTag("pacer");
                    if ( pacer != null ) {
                        pacer.deleteFromWorld();
                    }
                    e.removeTrigger();
                    break;
                case "robot-land":
                    if (robotOverlaps) {
                        robot.removeFlying().platform();
                        robot.script(OperationFactory.tween(new Angle(-30), new Angle(0), 1.5f));
                        e.removeTrigger();
                    }
                    break;
                case "farewell":
                    if (overlaps(player, e))
                    {
                        farewellSystem.start();
                    }
                    break;
                case "robot-hover":
                    if ( robotOverlaps ) {
                        robot.flying().removePlatform();
                        robot.script(OperationFactory.tween(new Angle(0), new Angle(-30), 1f));
                        e.removeTrigger();
                    }
                    break;
                case "music":
                    playMusic(e);
            }
        }
    }

    private void playMusic(E e) {
        String song = e.triggerParameter();
        if ( !song.equals(playingSong)) {
            playingSong = song;
            assetSystem.playMusicInGame(song);
        }
    }
}
