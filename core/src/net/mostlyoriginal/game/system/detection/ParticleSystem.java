package net.mostlyoriginal.game.system.detection;

import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.G;

import static com.badlogic.gdx.math.MathUtils.random;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class ParticleSystem extends PassiveSystem {

    private Color BLOOD_COLOR = Color.valueOf("4B1924");
    private Color COLOR_WHITE = Color.valueOf("FFFFFF");
    private Color COLOR_DUST = Color.valueOf("FFFFFFAA");

    private Builder bakery = new Builder();

    public void dust(float x, float y, float angle) {
        bakery
                .color(COLOR_DUST)
                .at(x, y)
                .angle(angle,angle)
                .speed(10, 15)
                .fadeAfter(0.1f)
                .rotateRandomly()
                .size(1, 3)
                .create(1,3);
    }

    public void bloodExplosion(float x, float y) {
        bakery
                .color(BLOOD_COLOR)
                .at(x, y)
                .angle(0, 360)
                .speed(20, 30)
                .slowlySplatDown()
                .size(1, 3)
                .solid()
                .create(80);
    }

    Vector2 v2 = new Vector2();

    public E spawnVanillaParticle(float x, float y, float angle, float speed, float scale) {

        v2.set(speed, 0).setAngle(angle);

        return E.E()
                .pos(x, y)
                .anim("particle")
                .scale(scale)
                .renderLayer(G.LAYER_PARTICLES)
                .origin(scale / 2f, scale / 2f)
                .bounds(0, 0, scale, scale)
                .physicsVx(v2.x)
                .physicsVy(v2.y)
                .physicsFriction(0);
    }

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
        private int minScale;
        private int maxScale;
        private boolean withSolid;
        private float gravityY;
        private float fadeDelay;

        private Tint tmpFrom = new Tint();
        private Tint tmpTo = new Tint();
        private float rotateR = 0;

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
                        random(minScale, maxScale))
                        .tint(color.r,color.g,color.b,color.a);

                if (withGravity) {
                    e.gravity();
                    e.gravityY(gravityY);
                }
                if (withSolid) {
                    e.mapWallSensor();
                }
                if (rotateR != 0) {
                    e.physicsVr(rotateR).angle();
                }
                if (fadeDelay > 0) {
                    e.script(sequence(
                            delay(fadeDelay),
                            JamOperationFactory.tintBetween(tmpFrom, tmpTo, 0.5f),
                            deleteFromWorld()
                    ));
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
            minScale = 1;
            maxScale = 1;
            gravityY = 1;
            fadeDelay = -1;
            withSolid = false;
            rotateR=0;
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

        public Builder size(int minScale, int maxScale) {
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
            rotateR = MathUtils.random(-100f,100f);
            return this;
        }
    }

}
