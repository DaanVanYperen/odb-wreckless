package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Spout;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;

/**
 * @author Daan van Yperen
 */
public class SpoutSystem extends FluidIteratingSystem {

    private ParticleSystem particleSystem;
    private EntitySpawnerSystem entitySpawnerSystem;

    public SpoutSystem() {
        super(Aspect.all(Spout.class, Pos.class));
    }

    Vector2 v2 = new Vector2();

    @Override
    protected void process(E e) {
        e.spoutAge(e.spoutAge() + world.delta);
        if (e.spoutAge() >= e.spoutCooldown() + e.spoutSprayDuration()) {
            e.spoutSprayCooldown(0);
            e.spoutAge(0);
        }
        if (e.spoutAge() >= e.spoutCooldown()) {
            e.spoutSprayCooldown(e.spoutSprayCooldown() - world.delta);
            if (e.spoutSprayCooldown() <= 0) {
                e.spoutSprayCooldown(e.spoutSprayInterval());
                float angle = e.spoutAngle() + MathUtils.random(-2f, 2f);
                v2.set(10, 0).setAngle(angle).add(e.posX() + e.boundsCx(), e.posY() + e.boundsCy());

                switch (e.spoutType()) {
                    case ACID:
                        spawnAcid(angle, v2.x, v2.y, e.spoutAngle() == 90 ? 50 : 20);
                        break;
                    case GREMLIN:
                        if (playerWithInRange(v2.x, v2.y)) {
                            spawnGremlin(angle, v2.x, v2.y);
                        }
                        break;
                }

            }
        }
    }

    private boolean playerWithInRange(float x, float y) {
        float range = entityWithTag("player").posXy().dst2(x, y, 0);
        return range < 224 * 224 && range > 48 * 48;
    }

    public void spawnGremlin(float angle, float x, float y) {
        entitySpawnerSystem.spawnGremlin(x - 12, y - 12);
    }

    private void spawnAcid(float angle, float x, float y, int force) {
        particleSystem.acid(x, y, angle, force);
    }
}
