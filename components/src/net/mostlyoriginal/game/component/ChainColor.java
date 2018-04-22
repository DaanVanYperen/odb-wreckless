package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public enum ChainColor {
    BLUE,
    GREEN,
    YELLOW,
    RED,
    PURPLE,
    ANY;

    public static boolean matches(ChainColor a, ChainColor b) {
        return a != null && b != null && (b == ANY || a == ANY || b == a);
    }
}
