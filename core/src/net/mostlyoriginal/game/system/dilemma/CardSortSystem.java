package net.mostlyoriginal.game.system.dilemma;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.component.CardData;
import net.mostlyoriginal.game.component.CardScript;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.PlayableCard;
import net.mostlyoriginal.game.component.ui.Clickable;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;
import static net.mostlyoriginal.api.operation.JamOperationFactory.moveBetween;
import static net.mostlyoriginal.api.operation.JamOperationFactory.scaleBetween;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * Responsible for serving and processing cards.
 *
 * @author Daan van Yperen
 */
public class CardSortSystem extends FluidIteratingSystem {

    public boolean shouldResort;

    public CardSortSystem() {
        super(Aspect.all(Pos.class, PlayableCard.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void begin() {
        super.begin();
        if (shouldResort) { // && onlyClickableCardsRemain()) {
            shouldResort = false;
            resortCards();
        }
    }

    private boolean onlyClickableCardsRemain() {
        IntBag entities = getEntityIds();
        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            if (!E.E(ids[i]).hasClickable()) return false;
        }
        return true;
    }


    @Override
    protected void process(E e) {
    }

    public void resortCards() {
        IntBag entities = getEntityIds();
        int[] ids = entities.getData();
        int count=0;
        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            if (e.hasClickable())
                count++;
        }
        float x = G.PLANET_CENTER_X - (count*33) - (MathUtils.clamp(count-1,0,99)*G.MARGIN_BETWEEN_CARDS);

        for (int i = 0, s = entities.size(); s > i; i++) {
            E e = E.E(ids[i]);
            if (!e.hasClickable())
                continue;
            if (e.posX() != x) {
                e.script(moveBetween(e.posX(), e.posY(), x, e.posY(), 1f, Interpolation.fade));
            }
            x += e.playableCardCard().width + G.MARGIN_BETWEEN_CARDS;
        }
    }
}
