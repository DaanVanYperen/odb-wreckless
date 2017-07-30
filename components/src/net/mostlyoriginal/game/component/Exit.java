package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Exit extends Component {
    float cooldown = 3;
    boolean broken=false;
    boolean open=false;
    public Exit() {
    }
}
