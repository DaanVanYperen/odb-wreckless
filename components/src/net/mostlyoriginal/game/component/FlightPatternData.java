
package net.mostlyoriginal.game.component;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class FlightPatternData implements Serializable {

    public String id;
    public String comment;
    public FlightPatternStep[] steps; // not used.

    public FlightPatternData() {
    }
}
