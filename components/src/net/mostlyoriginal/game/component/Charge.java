package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Charge extends Component {
    public float charge = 0;

    public Charge() {
    }

    public void increase(float c) {
        if (charge < 5) {
            charge += c;
            if (c > 5) charge = 5;
        }
    }

    public void decrease(float c) {
        if (charge > 0) {
            charge -= c;
            if (charge < 0) charge = 0;
        }
    }
}
