package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Deadly;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.SandSprinkler;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.badlogic.gdx.math.MathUtils.random;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class ParticleSystem extends FluidIteratingSystem {

    private Color BLOOD_COLOR = Color.valueOf("4B1924");
    private Color COLOR_WHITE = Color.valueOf("FFFFFF");
    private Color COLOR_DUST = Color.valueOf("D4CFB899");
    private Color COLOR_ACID = Color.valueOf("5F411CDD");
    private Color COLOR_LASER = Color.valueOf("FEE300");

    private Builder bakery = new Builder();
    private GameScreenAssetSystem assetSystem;
    private CameraSystem cameraSystem;

    public ParticleSystem() {
        super(Aspect.all(SandSprinkler.class));
    }


    public float cooldown = 0;

    public void sprinkleSand(int percentageChance) {
        for (E e : allEntitiesWith(SandSprinkler.class)) {
            if (MathUtils.random(0, 100f) <= percentageChance) {
                triggerSprinkler(e);
            }
        }
    }

    private void triggerSprinkler(E e) {
        for (int i = 0; i < MathUtils.random(1, 2); i++) {
            sand(e.posX() + MathUtils.random(0, e.boundsMaxx()), e.posY(), -90 + MathUtils.random(-2, 2), MathUtils.random(10, 40));
        }
    }


    public void dust(float x, float y, float angle) {
        bakery
                .color(COLOR_DUST)
                .at(x, y)
                .angle(angle, angle)
                .speed(10, 15)
                .fadeAfter(0.1f)
                .rotateRandomly()
                .size(1, 3)
                .create(1, 3);
    }

    public void sand(float x, float y, float angle, int force) {
        bakery
                .color(COLOR_LASER)
                .at(x, y)
                .angle(angle, angle)
                .speed(force, force + 5)
                .fadeAfter(1f + MathUtils.random(0f, 2f))
                .rotateRandomly()
                .size(1f, 2f)
                .solid()
                .create(1, 1);
    }


    public void bullet(float x, float y, float angle, int force, float x2, float y2, int team) {
        bakery
                .color(COLOR_LASER)
                .at(x, y)
                .emitterVelocity(x2,y2)
                .angle(angle, angle)
                .speed(force, force)
                //.deadly()
                .fadeAfter(1f)
                .deadly()
                .team(team)
//                .slowlySplatDown()
                .rotateRandomly()
                .size(2, 2)
                //.solid()
                .create(1, 1);
    }

    public void bloodExplosion(float x, float y) {
        assetSystem.playSfx("splat" + MathUtils.random(1, 4));
        bakery
                .color(BLOOD_COLOR)
                .at(x, y)
                .angle(0, 360)
                .speed(50, 80)
                .slowlySplatDown()
                .size(1, 3)
                .solid()
                .create(80);
    }

    Vector2 v2 = new Vector2();

    public E spawnVanillaParticle(float x, float y, float angle, float speed, float scale, float emitterVx, float emitterVy) {

        v2.set(speed, 0).setAngle(angle);

        return E.E()
                .pos(x - scale * 0.5f, y - scale * 0.5f)
                .anim("particle")
                .scale(scale)
                .renderLayer(G.LAYER_PARTICLES)
                .origin(scale / 2f, scale / 2f)
                .bounds(0, 0, scale, scale)
                .physicsVx(v2.x + emitterVx)
                .physicsVy(v2.y + emitterVy)
                .physicsFriction(0);
    }

    @Override
    protected void process(E e) {
    }

    @Override
    protected void begin() {
        cooldown -= world.delta;
        if (cooldown <= 0) {
            sprinkleSand(1);
            cooldown = 1f;
        }
    }

    float cooldown2 = 0;

    private class Builder {
        private Color color;
        private boolean withGravity;
        private int minX;
        private int maxX;
        private int minY;
        private int maxY;
        private float minAngle;
        private float maxAngle;
        private int minSpeed;
        private int maxSpeed;
        private float minScale;
        private float maxScale;
        private boolean withSolid;
        private float gravityY;
        private float fadeDelay;

        private Tint tmpFrom = new Tint();
        private Tint tmpTo = new Tint();
        private float rotateR = 0;
        private boolean withDeadly;
        private float emitterVx;
        private float emitterVy;
        private int team;

        public Builder() {
            reset();
        }

        Builder color(Color color) {
            this.color = color;
            return this;
        }

        void create(int count) {
            create(count, count);
        }

        void create(int minCount, int maxCount) {
            for (int i = 0, s = random(minCount, maxCount); i < s; i++) {
                final E e = spawnVanillaParticle(
                        random(minX, maxX),
                        random(minY, maxY),
                        random(minAngle, maxAngle),
                        random(minSpeed, maxSpeed),
                        random(minScale, maxScale),
                        emitterVx,emitterVy)
                        .tint(color.r, color.g, color.b, color.a);

                if (withGravity) {
                    e.gravity();
                    e.gravityY(gravityY);
                }
                if (withSolid) {
                    e.mapWallSensor();
                } else e.ethereal();
                if (withDeadly) {
                    e.deadly();
                }
                if (rotateR != 0) {
                    e.physicsVr(rotateR).angle();
                }
                if (fadeDelay > 0) {
                    e.script(sequence(
                            delay(fadeDelay),
                            remove(Deadly.class),
                            JamOperationFactory.tintBetween(tmpFrom, tmpTo, 0.5f),
                            deleteFromWorld()
                    ));
                }
                if ( team != 0)
                {
                    e.teamTeam(team);
                }
            }
            reset();
        }

        Builder slowlySplatDown() {
            this.withGravity = true;
            this.gravityY = -0.5f;
            return this;
        }

        private void reset() {
            color = COLOR_WHITE;
            withGravity = false;
            minX = 0;
            maxX = 0;
            minY = 0;
            maxY = 0;
            minAngle = 0;
            maxAngle = 0;
            minSpeed = 0;
            maxSpeed = 0;
            withDeadly = false;
            minScale = 1;
            maxScale = 1;
            gravityY = 1;
            fadeDelay = -1;
            withSolid = false;
            rotateR = 0;
        }

        public Builder angle(float minAngle, float maxAngle) {
            this.minAngle = minAngle;
            this.maxAngle = maxAngle;
            return this;
        }

        public Builder angle(int angle) {
            this.minAngle = angle;
            this.maxAngle = angle;
            return this;
        }

        public Builder speed(int minSpeed, int maxSpeed) {
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Builder speed(int speed) {
            this.minSpeed = speed;
            this.maxSpeed = speed;
            return this;
        }

        public Builder size(float minScale, float maxScale) {
            this.minScale = minScale;
            this.maxScale = maxScale;
            return this;
        }

        public Builder size(int size) {
            this.minScale = size;
            this.maxScale = size;
            return this;
        }

        public Builder solid() {
            withSolid = true;
            return this;
        }

        public Builder at(float x, float y) {
            minX = maxX = (int) x;
            minY = maxY = (int) y;
            return this;
        }

        public Builder fadeAfter(float delay) {
            this.fadeDelay = delay;
            tmpFrom = new Tint(color);
            tmpTo = new Tint(color);
            tmpTo.color.a = 0;
            return this;
        }

        public Builder rotateRandomly() {
            rotateR = MathUtils.random(-100f, 100f);
            return this;
        }

        public Builder deadly() {
            withDeadly = true;
            return this;
        }

        public Builder emitterVelocity(float emitterVx, float emitterVy) {
            this.emitterVx = emitterVx;
            this.emitterVy = emitterVy;
            return this;
        }

        public Builder team(int team) {
            this.team = team;
            return this;
        }
    }

}
