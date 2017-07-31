package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Socket extends Component {
    public int entityId;
    public String animSocketed;
    public String animEmpty;
    public String sfxSocketed = "MOWV";
    public String sfxUnsocketed="VWOM";

    public Socket() {
    }
}
