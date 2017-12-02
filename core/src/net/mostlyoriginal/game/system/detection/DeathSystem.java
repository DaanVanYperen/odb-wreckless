package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
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

/**
 * @author Daan van Yperen
 */
public class DeathSystem extends FluidIteratingSystem {

    public TransitionSystem transitionSystem;
    private FollowSystem followSystem;
    private MapCollisionSystem mapCollisionSystem;
    private EntitySpawnerSystem entitySpawnerSystem;
    private ParticleSystem particleSystem;
    private MyAnimRenderSystem animSystem;
    private GameScreenAssetSystem assetSystem;
    private DialogSystem dialogSystem;
    private CameraSystem cameraSystem;
    private EBag deadlies;

    public DeathSystem() {
        super(Aspect.all(Pos.class).one(Mortal.class));
    }

    @Override
    protected void begin() {
        super.begin();
        deadlies = allEntitiesWith(Deadly.class);
    }

    @Override
    protected void process(E e) {
//
//        E enemy = touchingDeadlyStuffs(e, false);
//        if (enemy != null) {
//            e.chargeDecrease(0.2f);
//            particleSystem.bloodExplosion(enemy.posX() + enemy.boundsCx(), enemy.posY() + enemy.boundsCy());
//            enemy.deleteFromWorld();
//        }

        if (!e.hasDead()) {
            if (mapCollisionSystem.isLava(e.posX(), e.posY()) || touchingDeadlyStuffs(e, false) != null) {
                e.dead();

            }

            float halfScreenWidth = (Gdx.graphics.getWidth() / G.CAMERA_ZOOM) * 0.5f + 16;

            if (e.hasRunning() && e.posX() + e.boundsMaxx() < cameraSystem.camera.position.x - halfScreenWidth) {
                e.dead();
            }
        } else {
            e.deadCooldown(e.deadCooldown() - world.delta);
            if (!e.hasInvisible()) {
                if (e.teamTeam() == 2) {
                    assetSystem.stopMusic();
                    assetSystem.playSfx("deathsound");
                    assetSystem.playSfx("death_jingle");
                    if (!e.isRobot()) {
                        dialogSystem.robotSay(DialogSystem.Dialog.SAD, 0.5f, 5f);
                    }
                } else {
                    assetSystem.playSfx("gremlin_death");
                }
                e.invisible();
                particleSystem.bloodExplosion(e.posX() + e.boundsCx(), e.posY() + e.boundsCy());
            }
            if (e.deadCooldown() <= 0) {
                if ((e.isRobot() || e.isShipControlled())) {
                    doExit();
                    e.removeDead().removeMortal();
                } else e.deleteFromWorld();
            }

        }
    }

    private E touchingDeadlyStuffs(E e, boolean onlyMortals) {
        for (E o : deadlies) {
            if (o != e && o.teamTeam() != e.teamTeam() && overlaps(o, e) && !o.hasDead() && (!onlyMortals || o.hasMortal())) return o;
        }

        return null;
    }

    private void doExit() {
        transitionSystem.transition(GameScreen.class, 0.1f);
    }
}
