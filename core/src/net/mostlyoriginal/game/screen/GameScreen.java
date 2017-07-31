package net.mostlyoriginal.game.screen;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.mouse.MouseCursorSystem;
import net.mostlyoriginal.api.system.physics.*;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.*;
import net.mostlyoriginal.game.system.detection.*;
import net.mostlyoriginal.game.system.map.*;
import net.mostlyoriginal.game.system.render.*;
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

                        new EntitySpawnerSystem(),
                        new MapSystem(),
                        new ParticleSystem(),
                        new PowerSystem(),

                        new GameScreenAssetSystem(),
                        new GameScreenSetupSystem(),

                        new CameraSystem(G.CAMERA_ZOOM),

                        new WallSensorSystem(),
                        new CollisionSystem(),
                        new TriggerSystem(),
                        new SpoutSystem(),


                        new FollowSystem(),
                        new PlayerControlSystem(),
                        new GravitySystem(),
                        new MapCollisionSystem(),
                        new PlatformCollisionSystem(),
                        new PhysicsSystem(),

                        new FootstepSystem(),

                        new CarriedSystem(),
                        new SocketSystem(),

                        new CameraFollowSystem(),
                        new CameraClampToMapSystem(),
                        new PriorityAnimSystem(),

                        new JumpAttackSystem(),

                        new ClearScreenSystem(Color.valueOf("031D1E")),
                        new MapRenderSystem(),

                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),

                        new MapRenderInFrontSystem(),
                        new TerminalSystem(),
                        new ExitSystem(),
                        new DeathSystem(),
                        new TransitionSystem(GdxArtemisGame.getInstance())
                ).build());
    }

}
