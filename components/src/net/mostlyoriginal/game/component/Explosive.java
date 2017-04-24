package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Explosive extends Component {
    public Explosive() {
    }

    public boolean primed = false;
    public int yield = 30;
}
