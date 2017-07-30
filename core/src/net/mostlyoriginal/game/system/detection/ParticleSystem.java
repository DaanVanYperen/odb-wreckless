package net.mostlyoriginal.game.system.detection;

import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.component.G;

/**
 * @author Daan van Yperen
 */
public class ParticleSystem extends PassiveSystem {

    private Color BLOOD_COLOR = Color.valueOf("4B1924");
    private Color COLOR_WHITE = Color.valueOf("FFFFFF");

    private Builder bakery = new Builder();

    public void bloodExplosion(float x, float y) {
        bakery
                .color(BLOOD_COLOR)
                .at( x, y)
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
        private int minAngle;
        private int maxAngle;
        private int minSpeed;
        private int maxSpeed;
        private int minScale;
        private int maxScale;
        private boolean withSolid;
        private float gravityY;

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
            for (int i = 0, s = MathUtils.random(minCount, maxCount); i < s; i++) {
                final E e = spawnVanillaParticle(
                        MathUtils.random(minX, maxX),
                        MathUtils.random(minY, maxY),
                        MathUtils.random(minAngle, maxAngle),
                        MathUtils.random(minSpeed, maxSpeed),
                        MathUtils.random(minScale, maxScale))
                        .tintColor(color);

                if (withGravity) {
                    e.gravity();
                    e.gravityY(gravityY);
                }
                if (withSolid) {
                    e.mapWallSensor();
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
            withSolid = false;
        }

        public Builder angle(int minAngle, int maxAngle) {
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

        public Builder at(float x,float y) {
            minX = maxX = (int)x;
            minY = maxY = (int)y;
            return this;
        }
    }

}
