package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Star;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class StarEffectSystem extends FluidIteratingSystem {

    public static final int BIGGEST_STAR_WIDTH = 100;

    /**
     * speed factor of ship.
     */
    private float timer;
    public float speedFactor;
    public boolean active=false;

    protected ComponentMapper<Star> mStar;
    protected ComponentMapper<Anim> mAnim;
    protected ComponentMapper<Pos> mPos;
    private int animStage;
    private int playSoundState = 0;

    public StarEffectSystem() {
        super(Aspect.all(Star.class, Pos.class, Anim.class));
    }

    @Override
    protected void initialize() {
        super.initialize();

        for (int i = 0; i < 200; i++) {
            spawnStar(randomStarX(), MathUtils.random(0, G.SCREEN_HEIGHT + BIGGEST_STAR_WIDTH), MathUtils.random(100) < 20 ? 1 : MathUtils.random(100) < 20 ? 0 : 2);
        }

    }


    private int randomStarX() {
        return MathUtils.random(0, G.SCREEN_WIDTH);
    }

    private void spawnStar(int x, int y, int kind) {
        E().pos(x, y)
                .star(kind)
                .anim()
                .angleRotation(90f)
                .renderLayer(G.LAYER_STAR)
                .tint(MathUtils.random(0.6f, 1f), MathUtils.random(0.6f, 1f), MathUtils.random(0.6f, 1f), MathUtils.random(kind == 0 ? 0.1f : 0.5f, 0.9f));
    }


    private void trustEffect() {

        // work towards full thrust.
        if (active) {
            timer += world.delta * 0.25f;
            timer = MathUtils.clamp(timer, 0f, 1f);
            speedFactor = Interpolation.exp5.apply(timer * 0.6f);
            if (playSoundState == 0) {
                playSoundState = 1;
            }
        } else {
            timer -= world.delta * 0.25f;
            timer = MathUtils.clamp(timer, 0f, 1f);
            speedFactor = Interpolation.exp5.apply(timer * 0.6f);
            if (playSoundState == 1) {
                playSoundState = 0;
            }
        }

    }

    @Override
    protected void begin() {
        super.begin();
        trustEffect();

        if (speedFactor > 0.5) {
            animStage = 3;
        } else if (speedFactor > 0.25) {
            animStage = 2;
        } else if (speedFactor > 0.05) {
            animStage = 1;
        } else animStage = 0;
    }

    @Override
    protected void process(E e) {

        // match animation to speed.
        Star star = e.getStar();
        Anim anim = e.getAnim();

        int id = 2 + animStage;
        if (animStage == 0) {
            // just blinking
            id = (int) (star.blinkTimer % 3f);
            star.blinkTimer += world.delta;
        }

        anim.id = star.animId[id];

        Pos pos = e.getPos();

        // move star to the left, and randomize location to give the appearance of more stars.
        pos.xy.y -= ((8f + (speedFactor * 500f)) * world.delta * star.speedFactor);
        if (pos.xy.y < -BIGGEST_STAR_WIDTH) {
            pos.xy.y = G.SCREEN_HEIGHT;
            pos.xy.x = randomStarX();
        }
    }
}
