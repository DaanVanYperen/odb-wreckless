package net.mostlyoriginal.game.system.dilemma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for all cards.
 */
public class CardLibrary {
    public Card[] cards;

    public CardLibrary() {
    }

    Map<String, List<Card>> grouped = new HashMap<String, List<Card>>();

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public Card getById(String id) {
        for (Card card : cards) {
            if (card.id != null && card.id.equals(id)) return card;
        }
        return null;
    }
}
