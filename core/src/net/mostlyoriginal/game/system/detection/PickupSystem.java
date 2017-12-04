package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.FollowSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class PickupSystem extends FluidIteratingSystem {

    private E player;
    private EntitySpawnerSystem entitySpawnerSystem;
    private GroupManager groupManager;
    private GameScreenAssetSystem gameScreenAssetSystem;

    public PickupSystem() {
        super(Aspect.all(Pos.class, Pickup.class).exclude(Frozen.class));
    }

    @Override
    protected void begin() {
        super.begin();
        player = entityWithTag("player");
    }

    @Override
    protected void process(E e) {
        if ( overlaps(e, player) ) {
            upgradeGuns(player);
            e.deleteFromWorld();
        }
    }

    public void upgradeGuns(E playerShip) {
        Player player = playerShip.getPlayer();
        player.upgradeLevel++;
        String mainGun = null;
        String bounceGun = null;
        switch (player.upgradeLevel) {
            case 1:
                mainGun = "minigun";
                bounceGun = "bouncegun";
                break;
            case 2:
                mainGun = "minigun";
                bounceGun = "bouncegun_r2";
                break;
            case 3:
                mainGun = "minigun_r2";
                bounceGun = "bouncegun_r3";
                break;
            case 4:
                mainGun = "minigun_r2";
                bounceGun = "bouncegun_r4";
                break;
            case 5:
                mainGun = "minigun_r3";
                bounceGun = "bouncegun_r5";
                break;
            case 6:
                mainGun = "minigun_r3";
                bounceGun = "bouncegun_r6";
                break;
            case 7:
                mainGun = "minigun_r3";
                bounceGun = "bouncegun_r7";
                break;
            case 8:
                mainGun = "minigun_r4";
                bounceGun = "bouncegun_r7";
                break;
            case 9:
                mainGun = "minigun_r5";
                bounceGun = "bouncegun_r7";
                break;
            case 10:
                mainGun = "minigun_r5";
                bounceGun = "bouncegun_r7";
                break;
            default: return;
        }

        gameScreenAssetSystem.playSfx("Misc_2");
        killOldGuns();
        entitySpawnerSystem.addArsenal(playerShip, "player-guns", G.TEAM_PLAYERS, 0, mainGun, false);
        entitySpawnerSystem.addArsenal(playerShip, "player-guns", G.TEAM_PLAYERS, 0, bounceGun, false);
    }

    private void killOldGuns() {
        for (Entity entity : groupManager.getEntities("player-guns")) {
            entity.deleteFromWorld();
        }
    }
}
