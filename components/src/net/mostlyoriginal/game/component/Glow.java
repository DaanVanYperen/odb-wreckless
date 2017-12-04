package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Glow extends Component {
    public String anim;
    public float age;
    public float pulseSpeed=1f;
    public float maxIntensity=0.6f;
    public float minIntensity=0.4f;
    public int extendPixels=4;
    public Glow() {
    }
}
