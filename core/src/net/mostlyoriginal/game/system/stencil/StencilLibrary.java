package net.mostlyoriginal.game.system.stencil;

/**
 * @author Daan van Yperen
 */
public class StencilLibrary {
    public StencilData[] stencils;

    public StencilLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public StencilData getById(String id) {
        for (StencilData stencil : stencils) {
            if (stencil.id != null && stencil.id.equals(id)) return stencil;
        }
        return null;
    }

}
