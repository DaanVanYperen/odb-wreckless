package net.mostlyoriginal.game.system.dilemma;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.artemis.E.E;

/**
 * Responsible for serving and processing cards.
 *
 * @author Daan van Yperen
 */
public class CardSystem extends PassiveSystem {

    private static TransitionSystem transitionSystem;
    private CardLibrary cardLibrary;
    private GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void initialize() {
        super.initialize();
        loadCards();
    }

    private void loadCards() {
        final Json json = new Json();
        cardLibrary = json.fromJson(CardLibrary.class, Gdx.files.internal("cards.json"));

        for (Card card : cardLibrary.cards) {
            spawnCard(card);
        }
    }

    private void spawnCard(Card card) {

        String cardGfx = "card" + card.id;
        gameScreenAssetSystem.add(cardGfx, card.x, card.y, card.width, card.height, 1);

        E()
                .anim(cardGfx)
                .pos(50, 50)
                .renderLayer(100);
    }

    private static void restartGame() {
        transitionSystem.transition(GameScreen.class, 0f);
    }
}
