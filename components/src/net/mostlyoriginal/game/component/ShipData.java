
package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class ShipData implements Serializable {

    public String id;
    public String comment; // not used.
    public String anim;
    public String arsenal;
    public int hp=1;

    public ShipData() {
    }
}
