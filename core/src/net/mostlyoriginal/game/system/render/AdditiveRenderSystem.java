package net.mostlyoriginal.game.system.render;

/**
 * @author Daan van Yperen
 */

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.component.Glow;

/**
 * Render and progress animations.
 *
 * @author Daan van Yperen
 * @see Anim
 */
@Wire
public class AdditiveRenderSystem extends EntityProcessingSystem {

    protected M<Pos> mPos;
    protected M<Tint> mTint;
    protected M<Angle> mAngle;
    protected M<Scale> mScale;
    protected M<Origin> mOrigin;

    protected CameraSystem cameraSystem;
    protected AbstractAssetSystem abstractAssetSystem;

    protected SpriteBatch batch;
    private Origin DEFAULT_ORIGIN = new Origin(0.5f, 0.5f);
    private M<Glow> mGlow;

    public AdditiveRenderSystem() {
        super(Aspect.all(Pos.class, Glow.class).exclude(Invisible.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(2000);
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    }

    @Override
    protected void end() {
        batch.end();
    }

    private Color c = new Color(Color.WHITE);

    @Override
    protected void process(Entity e) {
        //final Anim anim = mAnim.get(e);
        final Glow glow = mGlow.get(e);
        final Pos pos = mPos.get(e);
        final Angle angle = mAngle.getSafe(e, Angle.NONE);
        final float scale = mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin origin = mOrigin.getSafe(e, DEFAULT_ORIGIN);

        if ( glow.pulseSpeed > 0 ) {
            glow.age += world.delta * glow.pulseSpeed;
            c.a = Interpolation.linear.apply(glow.minIntensity,glow.maxIntensity,Math.abs((glow.age % 2)-1));
        } else c.a=1f;

        batch.setColor(c);

        if (glow.anim != null) drawAnimation(angle, origin, pos, glow.anim, scale, glow.extendPixels);
    }

    /**
     * Pixel perfect aligning.
     */
    public float roundToPixels(final float val) {
        // since we use camera zoom rounding to integers doesn't work properly.
        return ((int) (val * cameraSystem.zoom)) / (float) cameraSystem.zoom;
    }

    private void drawAnimation(final Angle angle, final Origin origin, final Pos position, String id, float scale, int extendPixels) {

        final Animation<TextureRegion> gdxanim = (Animation<TextureRegion>) abstractAssetSystem.get(id);
        if (gdxanim == null) return;

        final TextureRegion frame = gdxanim.getKeyFrame(0);

        float ox = (frame.getRegionWidth()+ extendPixels) * scale * (origin.xy.x);
        float oy = (frame.getRegionHeight()+ extendPixels) * scale * (origin.xy.y);
        if (angle.rotation != 0) {
            batch.draw(frame,
                    roundToPixels(position.xy.x- extendPixels),
                    roundToPixels(position.xy.y- extendPixels),
                    ox,
                    oy,
                    (frame.getRegionWidth()+ extendPixels) * scale,
                    (frame.getRegionHeight()+ extendPixels) * scale, 1, 1,
                    angle.rotation);
        } else {
            batch.draw(frame,
                    roundToPixels(position.xy.x)-extendPixels,
                    roundToPixels(position.xy.y)-extendPixels,
                    (frame.getRegionWidth()+ extendPixels) * scale,
                    (frame.getRegionHeight()+ extendPixels)  * scale);
        }
    }
}
