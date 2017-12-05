package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.detection.DialogSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class HealthUISystem extends BaseSystem {

    private CameraSystem cameraSystem;
    private SpriteBatch batch;
    private TagManager tagManager;
    private E player;
    private GameScreenAssetSystem assetSystem;
    private TextureRegion tick;
    private DialogSystem dialogSystem;

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(100);
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f,1f,1f,0.8f);
        player = E.E(tagManager.getEntityId("player"));
        tick = ((Animation<TextureRegion>)assetSystem.get("tick")).getKeyFrame(0);
    }

    @Override
    protected void end() {
        super.end();
        batch.end();
    }

    @Override
    protected void processSystem() {
        if ( player != null && !dialogSystem.isActive() )
        for (int i = 0, s = player.shieldHp()/5; i <s; i++) {
            batch.draw(tick, 4 + i * (tick.getRegionWidth()+2), 4, 4, 8);
        }
    }
}