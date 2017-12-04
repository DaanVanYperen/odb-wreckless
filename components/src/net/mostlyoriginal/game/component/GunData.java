
package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class GunData implements Serializable {

    public String anim;
    public String animbounced;
    public int angle;
    public int rpm;
    public int speed;
    public int bounces;
    public int damage;

    public GunData() {
    }
}
