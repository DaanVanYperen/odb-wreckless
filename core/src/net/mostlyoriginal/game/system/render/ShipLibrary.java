package net.mostlyoriginal.game.system.render;

import net.mostlyoriginal.game.component.ArsenalData;
import net.mostlyoriginal.game.component.GunData;
import net.mostlyoriginal.game.component.ShipData;

import java.io.Serializable;

/**
 * Repository for all cards.
 */
public class ShipLibrary implements Serializable {
    public ShipData[] ships;
    public ArsenalData[] arsenalData;

    public ShipLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public ShipData getById(String id) {
        for (ShipData ship : ships) {
            if (ship.id != null && ship.id.equals(id)) return ship;
        }
        return null;
    }
}
