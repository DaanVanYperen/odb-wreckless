package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Exit extends Component {
    public float cooldown = 3;
    public boolean broken=false;
    public boolean open=false;
    public Exit() {
    }
}
