package net.mostlyoriginal.api.system.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Gravity;
import net.mostlyoriginal.game.component.Carries;
import net.mostlyoriginal.game.component.Socket;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

@Wire
public class SocketSystem extends FluidIteratingSystem {

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

    public void socket(E e, E socket) {
        unsocket(e);
        socket.socketEntityId(e.socketedInsideEntityId(socket.id()).invisible().id());
    }

    public void unsocket(E e) {
        if (e.hasSocketedInside()) {
            E(e.getSocketedInside().entityId).socketEntityId(0);
            e.removeSocketedInside().removeInvisible();
        }
    }
}
