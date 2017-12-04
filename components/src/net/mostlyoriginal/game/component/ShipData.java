
package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class ShipData implements Serializable {

    public String id;
    public String comment; // not used.
    public String anim;
    public String shieldAnim;
    public String corpseAnim;
    public String arsenal;
    public String flight;
    public int layerOffset = 0;
    public float originX = 0.5f;
    public float originY = 0.5f;
    public int hp=1;

    public ShipData() {
    }
}
