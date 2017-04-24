package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * Created by Daan on 10-9-2014.
 */
public class CardData {

    public String id;
    public String comment; // not used.
    public ScriptCommand[] script;
    public String sfx;

    public boolean statuseffect = false;
    public boolean manual = false;

    public int x;
    public int y;
    public int width;
    public int height;

    public CardData() {
    }
}
