package net.mostlyoriginal.game.system.planet.physics;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.component.Died;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Ghost;
import net.mostlyoriginal.game.component.Planetbound;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class GhostSystem extends FluidIteratingSystem {

    private TagManager tagManager;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private PlanetCreationSystem planetCreationSystem;

    public GhostSystem() {
        super(Aspect.all(Ghost.class));
    }

    Vector2 v = new Vector2();

    @Override
    protected void process(E e) {
        e.ghostAge(e.ghostAge() + world.delta);
        if (e.ghostAge() > 0.5f) {
            if (e.hasReviveAfterDeath()) {
                E e2 = planetCreationSystem.spawnDude(e.posX(), e.posY());
                if (e.hasDolphinized()) {
                    e2.dolphinized();
                    gameScreenAssetSystem.playSfx("LD_rebornflipper");
                }
                e2.reviveAfterDeath();
            }
            e.removeGhost().deleteFromWorld();
        }
    }
}
