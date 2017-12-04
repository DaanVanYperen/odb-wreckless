
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
    public int x=0;
    public int speed;
    public int bounces;
    public int damage;
    public float cooldown=0;
    public float duration=0;

    public GunData() {
    }
}
