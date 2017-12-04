package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.Deadly;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Mortal;
import net.mostlyoriginal.game.component.Pickup;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.FollowSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class PickupSystem extends FluidIteratingSystem {

    private E player;

    public PickupSystem() {
        super(Aspect.all(Pos.class, Pickup.class).exclude(Frozen.class));
    }

    @Override
    protected void begin() {
        super.begin();
        player = entityWithTag("player");
    }

    @Override
    protected void process(E e) {
        if ( overlaps(e, player) ) {
            e.deleteFromWorld();
        }
    }
}
