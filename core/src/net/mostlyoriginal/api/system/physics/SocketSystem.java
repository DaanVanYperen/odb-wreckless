package net.mostlyoriginal.api.system.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Socket;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.PowerSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;

@Wire
public class SocketSystem extends FluidIteratingSystem {

    private PowerSystem powerSystem;
    private GameScreenAssetSystem assetSystem;

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
        socket.socketEntityId(battery.socketedInsideEntityId(socket.id()).invisible().id());
        power(socket, true);
        assetSystem.playSfx("MOWV");

        if ( socket.isRobot() ) {
            battery.deleteFromWorld();
            socket.chargeIncrease(G.BARS_FOR_BATTERY);
        }
    }

    private void power(E socket, boolean enable) {
        powerSystem.powerMapCoordsAround((int)(socket.posX()/ G.CELL_SIZE + 0.5f),(int)(socket.posY()/G.CELL_SIZE + 0.5f), enable);
    }

    public void unsocket(E e) {
        if (e.hasSocketedInside()) {
            E socket = E(e.getSocketedInside().entityId);
            power(socket, false);
            socket.socketEntityId(0);
            e.removeSocketedInside().removeInvisible();
            assetSystem.playSfx("VWOM");
        }
    }
}
