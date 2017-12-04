
package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class DialogData implements Serializable {

    public String id;
    public String music;
    public LineData[] lines;
    public boolean triggered = false;

    public DialogData() {
    }
}
