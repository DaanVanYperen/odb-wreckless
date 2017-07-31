package net.mostlyoriginal.game.system.render;

/**
 * @author Daan van Yperen
 */

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

/**
 * Render and progress animations.
 *
 * @author Daan van Yperen
 * @see Anim
 */
@Wire
public class MyAnimRenderSystem extends DeferredEntityProcessingSystem {

    protected M<Pos> mPos;
    protected M<Anim> mAnim;
    protected M<Tint> mTint;
    protected M<Angle> mAngle;
    protected M<Scale> mScale;
    protected M<Origin> mOrigin;

    protected CameraSystem cameraSystem;
    protected AbstractAssetSystem abstractAssetSystem;

    protected SpriteBatch batch;
    private Origin DEFAULT_ORIGIN= new Origin(0.5f, 0.5f);

    public MyAnimRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Pos.class, Anim.class, Render.class).exclude(Invisible.class), principal);
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
    }

    @Override
    protected void end() {
        batch.end();
    }

    protected void process(final int e) {

        final Anim anim   = mAnim.get(e);
        final Pos pos     = mPos.get(e);
        final Angle angle = mAngle.getSafe(e, Angle.NONE);
        final float scale = mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin origin = mOrigin.getSafe(e, DEFAULT_ORIGIN);

        batch.setColor(mTint.getSafe(e, Tint.WHITE).color);

        if ( anim.id != null ) drawAnimation(anim, angle, origin, pos, anim.id,scale);
        if ( anim.id2 != null ) drawAnimation(anim, angle,origin,  pos, anim.id2,scale);

        anim.age += world.delta * anim.speed;
    }

    /** Pixel perfect aligning. */
    public float roundToPixels(final float val) {
        // since we use camera zoom rounding to integers doesn't work properly.
        return ((int)(val * cameraSystem.zoom)) / (float)cameraSystem.zoom;
    }

    public void forceAnim(E e, String id) {
        Animation<TextureRegion> anim = abstractAssetSystem.get(id);
        e.priorityAnim(id, anim.getFrameDuration(), anim.getPlayMode() == Animation.PlayMode.LOOP);
        e.priorityAnimCooldown(anim.getFrameDuration() * anim.getKeyFrames().length);
        e.priorityAnimAge(0);
    }

    private void drawAnimation(final Anim animation, final Angle angle, final Origin origin, final Pos position, String id, float scale) {

        // don't support backwards yet.
        if ( animation.age < 0 ) return;

        final Animation<TextureRegion> gdxanim = (Animation<TextureRegion>) abstractAssetSystem.get(id);
        if ( gdxanim == null) return;

        final TextureRegion frame = gdxanim.getKeyFrame(animation.age, animation.loop);

        float ox = frame.getRegionWidth() * scale * origin.xy.x;
        float oy = frame.getRegionHeight() * scale * origin.xy.y;
        if ( animation.flippedX && angle.rotation == 0)
        {
            // mirror
            batch.draw(frame.getTexture(),
                    roundToPixels(position.xy.x),
                    roundToPixels(position.xy.y),
                    ox,
                    oy,
                    frame.getRegionWidth() * scale,
                    frame.getRegionHeight() * scale,
                    1f,
                    1f,
                    angle.rotation,
                    frame.getRegionX(),
                    frame.getRegionY(),
                    frame.getRegionWidth(),
                    frame.getRegionHeight(),
                    true,
                    false);

        } else if ( angle.rotation != 0 )
        {
            batch.draw(frame,
                    roundToPixels(position.xy.x),
                    roundToPixels(position.xy.y),
                    ox,
                    oy,
                    frame.getRegionWidth() * scale,
                    frame.getRegionHeight() * scale, 1, 1,
                    angle.rotation);
        } else {
            batch.draw(frame,
                    roundToPixels(position.xy.x),
                    roundToPixels(position.xy.y),
                    frame.getRegionWidth() * scale,
                    frame.getRegionHeight() * scale);
        }
    }
}
