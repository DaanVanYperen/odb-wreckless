package net.mostlyoriginal.api.system.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Socket;
import net.mostlyoriginal.game.component.Type;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.DialogSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.PowerSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;

@Wire
public class SocketSystem extends FluidIteratingSystem {

    private PowerSystem powerSystem;
    private GameScreenAssetSystem assetSystem;
    private EntitySpawnerSystem entitySpawnerSystem;
    private MyAnimRenderSystem animSystem;
    private DialogSystem dialogSystem;

    public SocketSystem() {
        super(Aspect.all(Socket.class, Anim.class));
    }

    @Override
    protected void process(E e) {
        if (e.socketEntityId() != 0) {
            if (e.socketAnimSocketed() != null) {
                e.anim(e.socketAnimSocketed());
            }
        } else {
            if (e.socketAnimEmpty() != null) {
                e.anim(e.socketAnimEmpty());
            }
        }
    }

    public void socket(E battery, E socket) {
        unsocket(battery);
        assetSystem.playSfx(socket.socketSfxSocketed());

        if (socket.isRobot()) {
            battery.deleteFromWorld();
            socket.chargeIncrease(G.BARS_FOR_BATTERY);
            if (socket.hasSlumbering()) {
                animSystem.forceAnim(socket, "robot-wake-up");
                socket.removeSlumbering();
                dialogSystem.playerSay(DialogSystem.Dialog.HAPPY, 2f,3f).robotSay(DialogSystem.Dialog.HAPPY, 1f,2f);
            } else {
                animSystem.forceAnim(socket, "robot-close-battery");
                if (MathUtils.random(1,100) < 25) {
                    dialogSystem.robotSay(DialogSystem.Dialog.HEART, 1.5f,3f);
                }
                if (MathUtils.random(1,100) < 25) {
                    dialogSystem.playerSay(DialogSystem.Dialog.HAPPY, 0.1f,3f);
                }
            }
        } else {
            socket.socketEntityId(battery.socketedInsideEntityId(socket.id()).invisible().id());
            power(socket, true);
        }
    }

    public void respawnRobotBatteries() {
        for (E socket : allEntitiesWith(Socket.class)) {
            if (socket.typeType().equals("battery2") && socket.socketEntityId() == 0 && !socket.isRobot()) {
                entitySpawnerSystem.spawnBatteryInSocket("battery2", socket);
            }
        }
    }

    private void power(E socket, boolean enable) {
        powerSystem.powerMapCoordsAround((int) (socket.posX() / G.CELL_SIZE + 0.5f), (int) (socket.posY() / G.CELL_SIZE + 0.5f), enable);
    }

    public void unsocket(E e) {
        if (e.hasSocketedInside()) {
            E socket = E(e.getSocketedInside().entityId);
            power(socket, false);
            socket.socketEntityId(0);
            e.removeSocketedInside().removeInvisible();
            assetSystem.playSfx(socket.socketSfxUnsocketed());
        }
    }
}
