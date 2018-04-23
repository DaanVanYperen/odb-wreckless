package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.component.Cashable;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.ScoreUISystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;
import static net.mostlyoriginal.api.utils.Duration.milliseconds;

/**
 * @author Daan van Yperen
 */
public class RewardSystem extends FluidIteratingSystem {

    private static final float FADEOUT_DURATION = 1f;
    private static final float GLOW_MAX_SCALE = 2f;
    private static final int SPACING_BETWEEN_BONUSES = 10;
    private static final int PITSTOP_LENGTH_BONUS = 2;
    private static final int MS_DELAY_PER_TEXTITEM = 40;
    private int bonusMultiplier;

    public RewardSystem() {
        super(Aspect.all(Cashable.class));
    }

    private TowedSystem towedSystem;

    int chainLengthShacklePoints[] = {0, 10, 20, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
    int chainLengthChainBonus[] = {0, 0, 0, 200, 500, 1000, 1500, 2000, 4000, 8000, 10000, 10000, 10000, 10000, 10000, 10000};
    int chainMulticolorBonus[] = {0, 0, 0, 200, 500, 1000, 1500, 2000, 4000, 8000, 10000, 10000, 10000, 10000, 10000, 10000};
    String chainMulticolorBonusRewardSound[] = {"chain_1", "chain_1", "chain_1", "chain_1", "chain_2", "chain_3", "chain_4", "chain_4", "chain_4", "chain_4", "chain_4", "chain_4", "chain_4"};
    String chainLenghBonusText[] = {
            " CHAIN BONUS!",
            " CHAIN BONUS!",
            " CHAIN BONUS!",
            " CHAIN BONUS!",
            " CHAIN BONUS! (x4)",
            " CHAIN BONUS! (x5)",
            " CHAIN BONUS! (x6)",
            " CHAIN BONUS! (x7)",
            " CHAIN BONUS! (x8)",
            " CHAIN BONUS! (x9)",
            " CHAIN BONUS! (x10)",
            " CHAIN BONUS! (x11)",
            " CHAIN BONUS! (x12)"};
    String pitstopChainBonusText[] = {
            " PITSTOP BONUS!",
            " MINI PITSTOP BONUS!",
            " TINY PITSTOP BONUS!",
            " PITSTOP BONUS! (x3)",
            " PITSTOP BONUS! (x4)",
            " PITSTOP BONUS! (x5)",
            " PITSTOP BONUS! (x6)",
            " PITSTOP BONUS! (x7)",
            " PITSTOP BONUS! (x8)",
            " PITSTOP BONUS! (x9)",
            " PITSTOP BONUS! (x10)",
            " PITSTOP BONUS! (x11)",
            " PITSTOP BONUS! (x12)"};

    int rewardCount = 0;
    int floaterIndex =0;
    float textDelay = MS_DELAY_PER_TEXTITEM;

    ScoreUISystem scoreUISystem;

    @Override
    protected void end() {
        super.end();
        bonusMultiplier = 0;
        floaterIndex=0;
    }

    @Override
    protected void process(E shackle) {
        shackle.cashableCooldown(shackle.cashableCooldown() - world.delta);

        
        textDelay = 1 + shackle.cashableCooldown();

//        if ( shackle.cashableCooldown() <= 0 )
        {
            final int x = GridSnapSystem.gridX(shackle) * G.CELL_SIZE;
            final int y = GridSnapSystem.gridY(shackle) * G.CELL_SIZE;

            rewardCount = 0;
            int multiplier = Math.max(1, shackle.cashableMultiplier() );
            multiplier = Math.max(1, multiplier + (bonusMultiplier - 1) );

            final boolean isPitstop = shackle.cashableType() == Cashable.Type.PITSTOP;
            final int bonusLength = isPitstop ? PITSTOP_LENGTH_BONUS : 0;

            final int totalLength = MathUtils.clamp(shackle.cashableChainLength() + bonusLength, 0, 12);
            rewardPoints(multiplier * chainLengthShacklePoints[totalLength], x, y + G.CELL_SIZE, SILVER);

            if (multiplier > 1) {
                textDelay = shackle.cashableCooldown() + 200 + MathUtils.random(50);
                G.sfx.playDelayed("rewardsound_3", milliseconds(textDelay));
                createFloaterLabel("X" + multiplier, x, y + G.CELL_SIZE, MathUtils.random(5, 10), MathUtils.random(60, 65), "italshuge", YELLOW);
            }

            if (shackle.cashableChainBonusPayout()) {
                textDelay = shackle.cashableCooldown() + 300+ MathUtils.random(50);
                G.sfx.playDelayed(chainMulticolorBonusRewardSound[totalLength], milliseconds(textDelay));
                rewardBonus(multiplier * chainLengthChainBonus[totalLength], x, y + G.CELL_SIZE + (rewardCount * SPACING_BETWEEN_BONUSES), isPitstop ? pitstopChainBonusText[totalLength - PITSTOP_LENGTH_BONUS] : chainLenghBonusText[totalLength], isPitstop ? GREEN : WHITE);
            }
            if (shackle.cashableChainMulticolorPayout()) {
                textDelay = shackle.cashableCooldown() + 500+ MathUtils.random(50);
                G.sfx.playDelayed("rewardsound_2", milliseconds(textDelay));
                rewardBonus(multiplier * chainMulticolorBonus[totalLength], x, y + G.CELL_SIZE + (rewardCount * SPACING_BETWEEN_BONUSES), " MULTICOLOR BONUS!", PURPLE);
            }

            sfx(shackle, x, y);
            killTows(shackle);
            shackle.removeCashable();
            shackle.script(deleteFromWorld());
        }
    }

    private void rewardPoints(int chainLengthShacklePoint, int x, int y, Tint targetColor) {
        payout(chainLengthShacklePoint, "" + chainLengthShacklePoint, x, y, MathUtils.random(5, 10), MathUtils.random(60, 65), "italsmall", targetColor);
    }

    private void rewardBonus(int chainLengthChainNonus, int x, int y, String suffix, Tint targetColor) {
        payout(chainLengthChainNonus, "+" + chainLengthChainNonus + suffix, x + 5, y, MathUtils.random(-10, 10), MathUtils.random(40, 45), "ital", targetColor);
    }

    private static final Tint YELLOW = new Tint("f6fd04aa");
    private static final Tint GREEN = new Tint("8ce902ee");
    private static final Tint WHITE = new Tint("ffffffee");
    private static final Tint SILVER = new Tint("ccccccee");
    private static final Tint PURPLE = new Tint("e121fcee");
    ;

    private void payout(int points, String label, int x, int y, int targetX, int targetY, String font, Tint targetColor) {
        if (points == 0) return;
        scoreUISystem.addPoints(points);
        createFloaterLabel(label, x, y, targetX, targetY, font, targetColor);
        rewardCount++;
    }

    private void createFloaterLabel(String label, int x, int y, int targetX, int targetY, String font, Tint targetColor) {
        E.E()
                .posX(x)
                .posY(y)
                .renderLayer(50000 + floaterIndex++)
                .fontFontName(font)
                .physicsFriction(0)
                .labelText(label)
                .tint(1f, 1f, 1f, 0f)
                .script(sequence(
                        delay(milliseconds(textDelay)),
                        JamOperationFactory.tintBetween(Tint.TRANSPARENT, targetColor, 0.1f),
                        JamOperationFactory.moveBetween(x, y, x + targetX, y + targetY, 0.5f, Interpolation.pow2Out),
                        deleteFromWorld()
                ));
        textDelay += MS_DELAY_PER_TEXTITEM;
    }

    private void sfx(E shackle, float x, float y) {
        G.sfx.play(MathUtils.randomBoolean() ? "pop_1" : "pop_2");
        E.E()
                .posX(x)
                .posY(y)
                .renderLayer(G.LAYER_PLAYER - 10)
                .anim(shackle.animId() + "-score")
                .script(sequence(
                        delay(milliseconds(250)),
                        deleteFromWorld()
                ));
    }

    private void killTows(E shackle) {
        if (shackle.hasTowing()) {
            // currenly cargo loaded.
            towedSystem.disconnectCargoFrom(shackle, true);
        }
        if (shackle.hasTowed()) {
            towedSystem.disconnectFromTowingCar(shackle, true);
        }
    }

    public void chainFinished() {
        bonusMultiplier++;
    }
}
