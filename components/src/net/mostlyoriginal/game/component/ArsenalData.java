
package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class ArsenalData implements Serializable {

    public String id;
    public GunData[] guns;
    public int damage;

    public ArsenalData() {
    }
}
