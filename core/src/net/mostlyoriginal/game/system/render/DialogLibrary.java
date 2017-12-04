package net.mostlyoriginal.game.system.render;

import net.mostlyoriginal.game.component.ArsenalData;
import net.mostlyoriginal.game.component.DialogData;

import java.io.Serializable;

/**
 * Repository for all cards.
 */
public class DialogLibrary implements Serializable {
    public DialogData[] dialog;

    public DialogLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public DialogData getById(String id) {
        for (DialogData a : dialog) {
            if (a.id != null && a.id.equals(id)) return a;
        }
        return null;
    }
}
