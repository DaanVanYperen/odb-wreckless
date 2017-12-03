package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class RenderBackgroundSystem extends BaseSystem {

    private SpriteBatch batch;
    private CameraSystem cameraSystem;
    private GameScreenAssetSystem assetSystem;

    private float lax1;
    private float lax2;

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(200);
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
    }

    @Override
    protected void processSystem() {
        TextureRegion stars = (TextureRegion) assetSystem.get("background-stars").getKeyFrame(0);
        TextureRegion clouds = (TextureRegion) assetSystem.get("background-clouds").getKeyFrame(0);

        lax1 += world.delta * 10f;
        lax2 += world.delta * 20f;

        if (lax1 >= stars.getRegionHeight()) lax1 -= stars.getRegionHeight();
        if (lax2 >= stars.getRegionHeight()) lax2 -= stars.getRegionHeight();

        for (int y = 0; y < (G.SCREEN_HEIGHT / stars.getRegionHeight()) + 2; y++) {
            for (int x = 0; x < (G.SCREEN_WIDTH / stars.getRegionWidth()) + 1; x++) {
                batch.draw(stars, x * stars.getRegionWidth(), y * stars.getRegionHeight() - (int) lax1);
            }
        }

        for (int y = 0; y < (G.SCREEN_HEIGHT / stars.getRegionHeight()) + 2; y++) {
            for (int x = 0; x < (G.SCREEN_WIDTH / stars.getRegionWidth()) + 1; x++) {
                batch.draw(clouds, x * clouds.getRegionWidth(), y * clouds.getRegionHeight() - (int) lax2);
            }
        }

    }

    @Override
    protected void end() {
        batch.end();
    }

}