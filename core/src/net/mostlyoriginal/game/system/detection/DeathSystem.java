package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
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

    public DeathSystem() {
        super(Aspect.all(Pos.class).one(Mortal.class, Robot.class));
    }

    @Override
    protected void process(E e) {

        if (e.hasRunning()) {
            particleSystem.gremlinWave();
        }

        if (e.hasRobot()) {
            if (e.chargeCharge() > 0) {
                E enemy = touchingDeadlyStuffs(e, true);
                if (enemy != null) {
                    e.chargeDecrease(0.2f);
                    particleSystem.bloodExplosion(enemy.posX() + enemy.boundsCx(), enemy.posY() + enemy.boundsCy());
                    enemy.deleteFromWorld();
                    animSystem.forceAnim(e, "robot-fight-stand");
                }
            }
        } else if (!e.hasDead()) {
            if (mapCollisionSystem.isLava(e.posX(), e.posY()) || touchingDeadlyStuffs(e, false) != null) {
                e.dead();
            }

            float halfScreenWidth = (Gdx.graphics.getWidth() / G.CAMERA_ZOOM) * 0.5f + 16;

            if (e.hasRunning() && e.posX() + e.boundsMaxx() < cameraSystem.camera.position.x - halfScreenWidth ) {
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
            if (e.deadCooldown() <= 0 ) {
                if ( (e.isRobot() || e.isPlayerControlled()) ) {
                    doExit();
                    e.removeDead().removeMortal();
                } else e.deleteFromWorld();
            }

        }
    }

    private E touchingDeadlyStuffs(E e, boolean onlyMortals) {

        for (E o : allEntitiesWith(Deadly.class)) {
            if (o == e) continue;
            if (overlaps(o, e) && o.teamTeam() != e.teamTeam() && (!onlyMortals || o.hasMortal())) return o;
        }

        return null;
    }

    private void doExit() {
        transitionSystem.transition(GameScreen.class, 0.1f);
    }
}
