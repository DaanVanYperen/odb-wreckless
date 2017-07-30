package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Trigger extends Component {
    public String trigger;

    public Trigger() {
    }

    public void set(String trigger) {
        this.trigger = trigger;
    }
}
