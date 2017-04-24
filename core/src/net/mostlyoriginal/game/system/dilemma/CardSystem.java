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
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.component.ui.Clickable;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.AchievementSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import java.security.Key;

import static com.artemis.E.E;
import static net.mostlyoriginal.api.operation.JamOperationFactory.moveBetween;
import static net.mostlyoriginal.api.operation.JamOperationFactory.moveTo;
import static net.mostlyoriginal.api.operation.JamOperationFactory.scaleBetween;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * Responsible for serving and processing cards.
 *
 * @author Daan van Yperen
 */
public class CardSystem extends FluidIteratingSystem {

    private static TransitionSystem transitionSystem;
    private CardLibrary cardLibrary;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private RenderBatchingSystem renderBatchingSystem;
    CardSortSystem cardSortSystem;
    private AchievementSystem achievementSystem;

    public CardSystem() {
        super(Aspect.all(Pos.class, PlayableCard.class, Clickable.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        loadCards();
    }


    private void loadCards() {
        final Json json = new Json();
        cardLibrary = json.fromJson(CardLibrary.class, Gdx.files.internal("cards.json"));
        dealRandomCards(3);
    }

    private void dealRandomCards(int count) {
        for (int i = 0; i < count; i++) {
            CardData card = cardLibrary.random();
            spawnCard(card);
        }
    }

    public void dealResetCard() {
        clearDeck();
        spawnCard(cardLibrary.getById("RESTART"));
    }

    private void clearDeck() {
        IntBag cards = getEntityIds();
        int[] ids = cards.getData();
        for (int i = 0, s = cards.size(); s > i; i++) {
            E e = E.E(ids[i]);
            e.removeClickable().script(sequence(moveBetween(e.posX(), e.posY(), e.posX(), e.posY() - 100, 1f, Interpolation.smooth), deleteFromWorld()));
        }
    }

    @Override
    protected void begin() {
        super.begin();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            //dealRandomCards(1);
            dealResetCard();
        }

        int cards = getEntityIds().size();
        if (cards <= 1 && !achievementSystem.gameEnded) {
            dealRandomCards(3-cards);
        }
    }

    @Override
    protected void process(E e) {

        e.scale(e.clickableState() == Clickable.ClickState.HOVER ? 1.2f : 1.0f);
        int layer = e.clickableState() == Clickable.ClickState.HOVER ? G.LAYER_CARDS_HOVER : G.LAYER_CARDS;
        if (e.renderLayer() != layer) {
            e.renderLayer(layer);
            renderBatchingSystem.sortedDirty = true;
        }
        if (e.clickableState() == Clickable.ClickState.CLICKED) {
            gameScreenAssetSystem.playSfx("LD1_event1");
            e.removeClickable();
            float MOVE_DURATION = 1.5f;
            float FINAL_CARD_SCALE = 0.4f;
            e.physicsVr(500f).physicsFriction(0).angle().tint(Tint.WHITE).renderLayer(G.LAYER_CARDS_FLYING);
            e.script(
                    sequence(
                            parallel(
                                    moveBetween(e.posX(), e.posY(), G.PLANET_CENTER_X - e.boundsCx() * FINAL_CARD_SCALE, G.PLANET_CENTER_Y - e.boundsCy() * FINAL_CARD_SCALE, MOVE_DURATION, Interpolation.smooth),
                                    scaleBetween(1f, FINAL_CARD_SCALE, MOVE_DURATION, Interpolation.smooth),
                                    sequence(
                                            delay(MOVE_DURATION - 0.2f),
                                            tween(Tint.WHITE, Tint.TRANSPARENT, 0.2f)
                                    )),
                            add(new CardScript(e.playableCardCard().script))
                    ));
            cardSortSystem.shouldResort = true;
        }
    }

    private void spawnCard(CardData card) {

        String cardGfx = "card" + card.id;
        gameScreenAssetSystem.add(cardGfx, card.x, card.y, card.width, card.height, 1);
        E()
                .anim(cardGfx)
                .clickable()
                .playableCardCard(card)
                .bounds(0, 0, card.width, card.height)
                .pos(-300, G.CARD_Y)
                .physics()
                .scale(1.0f)
                .renderLayer(G.LAYER_CARDS);
        cardSortSystem.shouldResort = true;
    }

    private static void restartGame() {
        transitionSystem.transition(GameScreen.class, 0f);
    }

    public void spawnCard(String id) {
        spawnCard(cardLibrary.getById(id));
    }
}
