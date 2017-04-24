package net.mostlyoriginal.game.system.dilemma;

import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.CardData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for all cards.
 */
public class CardLibrary {
    public CardData[] cards;

    public CardLibrary() {
    }

    Map<String, List<CardData>> grouped = new HashMap<String, List<CardData>>();

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public CardData getById(String id) {
        for (CardData card : cards) {
            if (card.id != null && card.id.equals(id)) return card;
        }
        return null;
    }

    public CardData random() {
        CardData result = null;
        while (result == null || result.manual) {
            result = cards[MathUtils.random(0, cards.length - 1)];
        }
        return result;
    }
}
