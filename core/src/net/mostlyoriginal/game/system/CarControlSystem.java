package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.DialogSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class CarControlSystem extends FluidIteratingSystem {
    private static final float RUN_SLOW_PACE_FACTOR = 500;
    public static final int SPEED_CLAMP = 300;
    private static final float RUN_FAST_PACE_FACTOR = SPEED_CLAMP;
    public static final float BRAKE_STRENGTH = 16f;
    private int MOVEMENT_FACTOR = 1;
    private float JUMP_FACTOR = 15000;
    //private SocketSystem socketSystem;
    private FollowSystem followSystem;
    private MyAnimRenderSystem animSystem;
    private GameScreenAssetSystem assetSystem;

    private GridSnapSystem gridSnapSystem;
    //
    // private DialogSystem dialogSystem;
    private GroupManager groupManager;
    private TowedSystem towedSystem;

    public boolean scrolling = true;


    public CarControlSystem() {
        super(Aspect.all(ShipControlled.class, WallSensor.class, Anim.class).exclude(Spinout.class));
    }

    @Override
    protected void process(E e) {
        if (world.delta == 0) return;


        String playerAnimPrefix = "player-";

        e.animId(playerAnimPrefix + "idle");
        e.angleRotation(0);
        e.physicsVr(0);

        int dx = 0;
        int dy = 0;

        fireGuns(e);


        e.animLoop(true);
        if (!e.hasDead()) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.E)) {
                e.shipControlledReleasing(true);
            } else {
                if ( e.shipControlledReleasing()) {
                    towedSystem.disconnectCargoFrom(e,true);
                }
                e.shipControlledReleasing(false);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    e.animAge(0);
                }
                dx = -MOVEMENT_FACTOR;
                e.animId("player-left");
                e.animLoop(false);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    e.animAge(0);
                }
                dx = MOVEMENT_FACTOR;
                e.animId("player-right");
                e.animLoop(false);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                dy = MOVEMENT_FACTOR;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                dy = -MOVEMENT_FACTOR;
            }
        }

        if (!G.PRODUCTION) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) MapCollisionSystem.DEBUG = !MapCollisionSystem.DEBUG;
        }

        // if starting to release tows and moving up and down initiates a drift.
        if ( e.shipControlledReleasing() && dy != 0 )
        {
            e.driftingDy(dy);
        } else {e.removeDrifting();}

        //whistle(e, playerAnimPrefix);

        e.shipControlledDx(dx * 3);
        e.shipControlledDy(dy * 3);
        gridSnapSystem.moveRelativeToSelf(e, dx * 3, dy * 3);

//
//        if (scrolling) {
//            e.posX(e.posX() + G.CAMERA_SCROLL_SPEED * world.delta);
//        }
//        entityWithTag("camera")
//                .physicsVx(scrolling ? G.CAMERA_SCROLL_SPEED : 0);
//
        entityWithTag("camera").posY(e.posY());
    }

    private void fireGuns(E e) {
        boolean firing = !e.hasDead() && (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.E));

        ImmutableBag<Entity> entities = groupManager.getEntities("player-guns");
        for (Entity entity : entities) {
            E.E(entity.getId())
                    .shooting(firing).tint(1f, 1f, 1f, 1f)
                    .physicsVx(e.physicsVx())
                    .physicsVy(e.physicsVy())
                    .tintColor().b = firing ? 1f : 0;
        }
    }

    private void whistle(E e, String playerAnimPrefix) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (e.hasCarries()) {
                E socket = firstTouchingEntityMatching(e, Aspect.all(Socket.class));
                if (socket != null) {
                    socketCarried(e, socket);
                } else {
                    callRobot(e);
                    animSystem.forceAnim(e, playerAnimPrefix + "whistles");
                    //dropCarried(e);
                }
            } else {
                E pickup = firstTouchingEntityMatching(e, Aspect.all(Pickup.class));
                if (pickup != null) {
                    carryItem(e, pickup);
                }
            }
        }
    }

    private void clampX(E e, float dx) {
        float veloX = Math.abs(e.physicsVx());
        if (Math.abs(dx) < 0.05f && veloX >= 0.1f) {
            e.physicsVx(e.physicsVx() - (e.physicsVx() * world.delta * BRAKE_STRENGTH));
        }
    }

    private void clampY(E e, float dy) {
        float veloY = Math.abs(e.physicsVy());
        if (Math.abs(dy) < 0.05f && veloY >= 0.1f) {
            e.physicsVy(e.physicsVy() - (e.physicsVy() * world.delta * BRAKE_STRENGTH));
        }
    }

    private void socketCarried(E e, E socket) {
        if (e.hasCarries() && socket.socketEntityId() == 0) {
            E battery = E.E(e.getCarries().entityId);
            if (battery.typeType().equals(socket.typeType())) {
                //socketSystem.socket(battery, socket);
                e.removeCarries();
            }
        }
    }

    private void dropCarried(E e) {
        if (e.hasCarries()) {
            E.E(e.getCarries().entityId).gravity();
            e.removeCarries();
        }
    }

    private void carryItem(E e, E pickup) {
        if (pickup.hasSocketedInside()) {
            //socketSystem.unsocket(pickup);
        }
        e.carriesEntityId(pickup.id());
        e.carriesAnchorY((int) e.boundsMaxy() - 4);
        pickup.removeGravity();
    }

    private void callRobot(E e) {
        followSystem.createMarker(e);
        assetSystem.playSfx("voice1");
    }
}
