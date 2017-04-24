package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class GhostSystem extends FluidIteratingSystem {

    private TagManager tagManager;
    private GameScreenAssetSystem gameScreenAssetSystem;

    public GhostSystem() {
        super(Aspect.all(Planetbound.class, Died.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        E.E()
                .pos(e.getPos())
                .anim("dudeghost")
                .renderLayer(G.LAYER_GHOST)
                .orientToGravity()
                .orientToGravityIgnoreFloor(true)
                .angle()
                .tint(Tint.WHITE)
                .physicsVelocity(e.planetboundGravity().x * -100f, e.planetboundGravity().y * -100f, 0f)
                .script(
                        sequence(delay(0.5f), tween(Tint.WHITE, Tint.TRANSPARENT, 1f), deleteFromWorld()));
        gameScreenAssetSystem.playSfx("LD_troop_no");
        e.deleteFromWorld();
    }
}
