package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.component.ui.Clickable;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.dilemma.CardSystem;
import net.mostlyoriginal.game.system.planet.PlanetSimulationSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class AchievementSystem extends FluidIteratingSystem {
    public static final int DEGREE_SPACING = 20;
    private TagManager tagManager;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private PlanetSimulationSystem planetSimulationSystem;
    private Vector2 v = new Vector2();
    public boolean gameEnded = false;
    private CardSystem cardSystem;
    private DrawingSystem drawingSystem;


    public AchievementSystem() {
        super(Aspect.all(Achievement.class));
    }

    @Override
    protected void initialize() {
        super.initialize();

        if (!G.DEBUG_NO_ACHIEVEMENTS) {
            createAchievement(0, "achievement1");
            createAchievement(1, "achievement2");
            createAchievement(2, "achievement3");
            createAchievement(3, "achievement4");

            if (G.DEBUG_ACHIEVEMENTS) {
                activateAchievement("achievement1");
                activateAchievement("achievement2");
                activateAchievement("achievement3");
                activateAchievement("achievement4");
            }
        }
    }

    @Override
    protected void begin() {
        super.begin();
        if (dudeCount() == 0) {
            activateAchievement("achievement1");
            if (!gameEnded) {
                cardSystem.dealResetCard();
            }
            gameEnded = true;

        }
        if (hasDolphins() && !hasNonDolphins() && isLargelyWater()) {
            activateAchievement("achievement2");
        }
        if (isLargelyLava()) {
            activateAchievement("achievement3");
        }
        if (isLargelyGone()) {
            activateAchievement("achievement4");
        }
    }

    private boolean isLargelyWater() {
        float waterBlocks = planetSimulationSystem.simulatedBlocks[PlanetCell.CellType.WATER.ordinal()];
        float totalBlocks = planetSimulationSystem.totalSimulatedBlocks();
        return (totalBlocks > 0) && (waterBlocks / totalBlocks > 0.40f);
    }

    private boolean isLargelyLava() {
        float lavaBlocks = planetSimulationSystem.simulatedBlocks[PlanetCell.CellType.LAVA.ordinal()] + planetSimulationSystem.simulatedBlocks[PlanetCell.CellType.LAVA_CRUST.ordinal()];
        float totalBlocks = planetSimulationSystem.totalSimulatedBlocks();
        return (totalBlocks > 0) && lavaBlocks > 8000 && (lavaBlocks / totalBlocks > 0.30f);
    }

    private boolean isLargelyGone() {
        float totalBlocks = planetSimulationSystem.totalSimulatedBlocks();
        return (totalBlocks < 25000);
    }

    private void activateAchievement(String achievementId) {
        E e = E.E(tagManager.getEntityId(achievementId));
        if (!e.hasAchievement()) {
            e
                    .tint(1f, 1f, 1f, 1f)
                    .clickable()
                    .bounds(0, 0, 32, 32)
                    .achievementId(achievementId);
            gameScreenAssetSystem.playSfx("LD_troop_amazing");
            cardSystem.spawnCard(achievementId.toUpperCase());
        }
    }

    private void createAchievement(int count, String id) {
        if (!tagManager.isRegistered(id)) {
            v.set(180, 0).rotate(DEGREE_SPACING * 1.5f - count * DEGREE_SPACING).add(G.PLANET_CENTER_X, G.PLANET_CENTER_Y).add(-16, -16);
            E.E().pos(v.x, v.y)
                    .anim(id)
                    .tint(1f, 1f, 1f, 0.25f)
                    .tag(id)
                    .renderLayer(G.LAYER_ACHIEVEMENTS);
        }
    }

    private int dudeCount() {
        return getDudes().size();
    }

    private IntBag getDudes() {
        return world.getAspectSubscriptionManager().get(Aspect.all(Wander.class)).getEntities();
    }

    private boolean hasDolphins() {
        IntBag dudes = getDudes();

        int[] ids = dudes.getData();
        for (int i = 0, s = dudes.size(); s > i; i++) {
            E e = E.E(ids[i]);
            if (e.hasDolphinized()) return true;
        }
        return false;
    }

    private boolean hasNonDolphins() {
        IntBag dudes = getDudes();

        int[] ids = dudes.getData();
        for (int i = 0, s = dudes.size(); s > i; i++) {
            E e = E.E(ids[i]);
            if (!e.hasDolphinized()) return true;
        }
        return false;
    }

    @Override
    protected void process(E e) {
        if (e.hasClickable()) {
            e.scale(e.clickableState() == Clickable.ClickState.HOVER ? 1.5f : 1f);
            if (e.clickableState() == Clickable.ClickState.CLICKED) {
                PlanetCell.CellType type = null;
                if ("achievement1".equals(e.achievementId())) type = null;
                else if ("achievement2".equals(e.achievementId())) type = PlanetCell.CellType.WATER;
                else if ("achievement3".equals(e.achievementId())) type = PlanetCell.CellType.LAVA;
                else if ("achievement4".equals(e.achievementId())) type = PlanetCell.CellType.AIR;
                drawingSystem.startDrawing(type);
            }
        }
    }
}
