package net.mostlyoriginal.game.system.dilemma;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.PlayableCard;
import net.mostlyoriginal.game.component.ui.Clickable;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;

/**
 * Responsible for serving and processing cards.
 *
 * @author Daan van Yperen
 */
public class CardSystem extends FluidIteratingSystem {

    private static TransitionSystem transitionSystem;
    private CardLibrary cardLibrary;
    private GameScreenAssetSystem gameScreenAssetSystem;

    public CardSystem() {
        super(Aspect.all(Pos.class, PlayableCard.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        loadCards();
    }


    private void loadCards() {
        final Json json = new Json();
        cardLibrary = json.fromJson(CardLibrary.class, Gdx.files.internal("cards.json"));

        int x = (int) G.CARD_X;
        for (net.mostlyoriginal.game.component.Card card : cardLibrary.cards) {
            spawnCard(card, x);
            x += card.width + G.MARGIN_BETWEEN_CARDS;
        }
    }

    @Override
    protected void process(E e) {
        e.scale(e.clickableState() == Clickable.ClickState.HOVER ? 1.2f : 1.0f);
        e.renderLayer(e.clickableState() == Clickable.ClickState.HOVER ? 110 : 100);
        if (e.clickableState() == Clickable.ClickState.CLICKED) {
            play(e.getPlayableCard());
            e.deleteFromWorld();
        }
    }

    private void play(PlayableCard playableCard) {

    }

    private void spawnCard(net.mostlyoriginal.game.component.Card card, float x) {

        String cardGfx = "card" + card.id;
        gameScreenAssetSystem.add(cardGfx, card.x, card.y, card.width, card.height, 1);

        E()
                .anim(cardGfx)
                .clickable()
                .playableCardCard(card)
                .bounds(0, 0, card.width, card.height)
                .pos(x, G.CARD_Y)
                .scale(1.0f)
                .renderLayer(100);
    }

    private static void restartGame() {
        transitionSystem.transition(GameScreen.class, 0f);
    }
}
