package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Footsteps extends Component {
    public float stepSize = 6;
    public float lastX = -1;
    public String sfx;
    public float sfxCount = 0;
    public Footsteps() {
    }
}
