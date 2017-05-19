package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.planet.PlanetCreationSystem;
import net.mostlyoriginal.game.system.planet.PlanetRenderGravityDebugSystem;
import net.mostlyoriginal.game.system.planet.PlanetRenderTemperatureDebugSystem;

import static net.mostlyoriginal.game.component.G.PLANET_X;
import static net.mostlyoriginal.game.component.G.PLANET_Y;

/**
 * @author Daan van Yperen
 */
public class ExplosiveSystem extends FluidIteratingSystem {

    DrawingSystem drawingSystem;
    PlanetCreationSystem planetCreationSystem;

    public ExplosiveSystem() {
        super(Aspect.all(Explosive.class, Pos.class));
    }

    private boolean leftMousePressed;

    Vector2 v2 = new Vector2();
    Vector2 v3 = new Vector2();

    @Override
    protected void process(E e) {
        if (e.explosivePrimed()) {
            int cx = (int) (e.posX() + e.boundsCx());
            int cy = (int) (e.posY() + e.boundsCy());
            drawingSystem.draw(planetCreationSystem.planetEntity,
                    cx, cy,
                    e.explosiveYield(),
                    PlanetCell.CellType.FIRE);
            e.deleteFromWorld();

            for (E wanderer : allEntitiesMatching(Aspect.all(Wander.class, Pos.class, Physics.class))) {

                v2.set(wanderer.posX(), wanderer.posY()).sub(cx, cy);

                float len = Math.abs(v2.len());
                if (len < 100) {
                    float strength = MathUtils.clamp(0, 220 - len, 100);
                    v2.nor().scl(strength);

                    v3.set(wanderer.posX(), wanderer.posY()).sub(G.PLANET_CENTER_X, G.PLANET_CENTER_Y).nor().scl(strength / 2);

                    v2.add(v3);

                    wanderer.physicsVx(v2.x);
                    wanderer.physicsVy(v2.y);
                }
            }
        }
    }
}
