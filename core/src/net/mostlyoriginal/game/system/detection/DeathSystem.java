package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Dead;
import net.mostlyoriginal.game.component.Deadly;
import net.mostlyoriginal.game.component.Mortal;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.FollowSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;

/**
 * @author Daan van Yperen
 */
public class DeathSystem extends FluidIteratingSystem {

    public TransitionSystem transitionSystem;
    private FollowSystem followSystem;
    private MapCollisionSystem mapCollisionSystem;
    private EntitySpawnerSystem entitySpawnerSystem;
    private ParticleSystem particleSystem;

    public DeathSystem() {
        super(Aspect.all(Mortal.class, Pos.class));
    }

    @Override
    protected void process(E e) {
        if (!e.hasDead()) {
            if (mapCollisionSystem.isLava(e.posX(), e.posY()) || touchingDeadlyStuffs(e)) {
                e.dead();
            }
        } else {
            e.deadCooldown(e.deadCooldown() - world.delta);
            if (!e.hasInvisible()) {
                e.invisible();
                particleSystem.bloodExplosion(e.posX() + e.boundsCx(), e.posY() + e.boundsCy());
            }
            if (e.deadCooldown() <= 0) {
                doExit();
                e.removeDead().removeMortal();
            }
        }

    }

    private boolean touchingDeadlyStuffs(E e) {

        for (E o : allEntitiesWith(Deadly.class)) {
            if (overlaps(o, e)) return true;
        }

        return false;
    }

    private void doExit() {
        transitionSystem.transition(GameScreen.class, 0.1f);
    }
}
