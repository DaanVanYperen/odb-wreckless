package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Mass extends Component {
    public Mass() {
    }
    public Mass(float density) {
        this.density=density;
    }
    public float density=1f;
    public boolean inverse=false;
}
