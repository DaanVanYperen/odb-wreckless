package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Cashable extends Component {
    public Cashable() {
    }
    public int chainLength;
    public boolean chainBonusPayout;
    public boolean chainMulticolorPayout;
    public Type type;
    public float cooldown;
    public int multiplier=1;

    public enum Type {
        COLOR,
        PITSTOP
    }
}
