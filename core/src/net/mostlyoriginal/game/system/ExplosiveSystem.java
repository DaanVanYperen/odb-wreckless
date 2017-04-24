package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Explosive;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
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

    @Override
    protected void process(E e) {
        if (e.explosivePrimed()) {
            drawingSystem.draw(planetCreationSystem.planetEntity,
                    (int) (e.posX() + e.boundsCx()), (int) (e.posY() + e.boundsCy()),
                    e.explosiveYield(),
                    PlanetCell.CellType.FIRE);
            e.deleteFromWorld();
        }
    }
}
