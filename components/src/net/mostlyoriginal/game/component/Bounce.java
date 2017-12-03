package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Bounce extends Component {
    public int count;
    public int lastEntityId = -1;

    public Bounce() {
    }
}
