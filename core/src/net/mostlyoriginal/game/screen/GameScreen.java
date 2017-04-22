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
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.system.DrawingSystem;
import net.mostlyoriginal.game.system.dilemma.CardSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.planet.*;
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

    public static final String BACKGROUND_COLOR_HEX = "969291";

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
                        new TransitionSystem(GdxArtemisGame.getInstance()),

                        new CameraSystem(1),
                        new ClearScreenSystem(Color.valueOf(BACKGROUND_COLOR_HEX)),
                        new GameScreenAssetSystem(),
                        new GameScreenSetupSystem(),

                        new MouseCursorSystem(),

                        new PlanetCreationSystem(),

                        new DrawingSystem(),

                        new PlanetSimulationSystem(),
                        new PlanetRenderSystem(),
                        new PlanetRenderGravityDebugSystem(),
                        new PlanetRenderTemperatureDebugSystem(),

                        renderBatchingSystem = new RenderBatchingSystem(),
                        new AnimRenderSystem(renderBatchingSystem)
                ).build());
    }

}
