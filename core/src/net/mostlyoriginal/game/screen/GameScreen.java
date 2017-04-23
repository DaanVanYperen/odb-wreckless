package net.mostlyoriginal.game.screen;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.mouse.MouseCursorSystem;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.api.system.physics.PhysicsSystem;
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.DrawingSystem;
import net.mostlyoriginal.game.system.dilemma.CardScriptSystem;
import net.mostlyoriginal.game.system.dilemma.CardSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.planet.*;
import net.mostlyoriginal.game.system.planet.physics.GravitySystem;
import net.mostlyoriginal.game.system.planet.physics.OrientToGravitySystem;
import net.mostlyoriginal.game.system.planet.physics.WanderSystem;
import net.mostlyoriginal.game.system.stencil.PlanetStencilSystem;
import net.mostlyoriginal.game.system.ui.MouseClickSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.GameScreenSetupSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;
import net.mostlyoriginal.plugin.ProfilerPlugin;

/**
 * Example main game screen.
 *
 * @author Daan van Yperen
 */
public class GameScreen extends WorldScreen {

    public static final String BACKGROUND_COLOR_HEX = "0000FF";

    @Override
    protected World createWorld() {
        RenderBatchingSystem renderBatchingSystem;
        return new World(new WorldConfigurationBuilder()
                .dependsOn(EntityLinkManager.class, ProfilerPlugin.class, OperationsPlugin.class)
                .with(
                        new SuperMapper(),
                        new TagManager(),

                        new GroupManager(),
                        new CardSystem(),
                        new CardScriptSystem(),
                        new CollisionSystem(),
                        new TransitionSystem(GdxArtemisGame.getInstance()),

                        new CameraSystem(G.CAMERA_ZOOM),
                        new GameScreenAssetSystem(),
                        new GameScreenSetupSystem(),

                        new MouseCursorSystem(),
                        new MouseClickSystem(),
                        new WanderSystem(),

                        new PlanetCreationSystem(),

                        new DrawingSystem(),

                        new PlanetStencilSystem(),
                        new PlanetSimulationSystem(),

                        new OrientToGravitySystem(),
                        new GravitySystem(),
                        new PhysicsSystem(),

                        new PlanetBackgroundRenderSystem(),
                        new PlanetRenderSystem(),
                        new PlanetRenderGravityDebugSystem(),
                        new PlanetRenderTemperatureDebugSystem(),

                        renderBatchingSystem = new RenderBatchingSystem(),
                        new AnimRenderSystem(renderBatchingSystem)
                ).build());
    }

}
