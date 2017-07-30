package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class PriorityAnim extends Component {
    public float cooldown = 0.6f;
    public String animId;
    public float age=0;
    public boolean loop = false;

    public PriorityAnim() {
    }

    public void set(String anim, float cooldown, boolean loop) {
        this.animId = anim;
        this.cooldown = cooldown;
        this.loop = loop;
        this.age = age;
    }
}