package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class DeathSystem extends FluidIteratingSystem {

    private TagManager tagManager;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private PlanetCreationSystem planetCreationSystem;

    public DeathSystem() {
        super(Aspect.all(Planetbound.class, Died.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        E ghost = E.E()
                .pos(e.getPos())
                .anim("dudeghost")
                .renderLayer(G.LAYER_GHOST)
                .orientToGravity()
                .orientToGravityIgnoreFloor(true)
                .ghost()
                .angle()
                .tint(Tint.WHITE)
                .physicsVelocity(e.planetboundGravity().x * -100f, e.planetboundGravity().y * -100f, 0f)
                .script(
                        sequence(delay(0.5f), tween(Tint.WHITE, Tint.TRANSPARENT, 1f)));

        if (e.hasDolphinized()) {
            ghost.dolphinized();
        }

        if (e.hasReviveAfterDeath()) {
            ghost.reviveAfterDeath();
        }

        gameScreenAssetSystem.playSfx(e.hasDolphinized() ? (MathUtils.randomBoolean() ? "LD_ghostflipper" : "LD_ghostflipper2") : "LD_troop_no");
        e.deleteFromWorld();
    }
}
