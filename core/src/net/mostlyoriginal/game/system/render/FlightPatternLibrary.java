package net.mostlyoriginal.game.system.render;

import net.mostlyoriginal.game.component.FlightPattern;
import net.mostlyoriginal.game.component.FlightPatternData;
import net.mostlyoriginal.game.component.ShipData;

import java.io.Serializable;

/**
 * Repository for all cards.
 */
public class FlightPatternLibrary implements Serializable {
    public FlightPatternData[] patterns;

    public FlightPatternLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public FlightPatternData getById(String id) {
        for (FlightPatternData pattern : patterns) {
            if (pattern.id != null && pattern.id.equals(id)) return pattern;
        }
        return null;
    }
}
