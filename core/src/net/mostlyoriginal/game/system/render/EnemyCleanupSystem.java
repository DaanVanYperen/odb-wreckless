package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Frozen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Team;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * Unfreeze near camera.
 *
 * @author Daan van Yperen
 */
public class EnemyCleanupSystem extends FluidIteratingSystem {

    CameraSystem cameraSystem;
    private MyAnimRenderSystem myAnimRenderSystem;
    private boolean lockCamera;
    private CameraFollowSystem cameraFollowSystem;
    private float minX;

    public EnemyCleanupSystem() {
        super(Aspect.all(Pos.class, Team.class));
    }

    @Override
    protected void begin() {
        super.begin();
        final E camera = entityWithTag("camera");
        minX = cameraFollowSystem.minCameraX() - 60;
    }

    @Override
    protected void process(E e) {
        if ( e.teamTeam() == G.TEAM_ENEMIES && e.posX() <= minX && !e.hasTowed() ) {
            e.deleteFromWorld();
        }
    }
}

