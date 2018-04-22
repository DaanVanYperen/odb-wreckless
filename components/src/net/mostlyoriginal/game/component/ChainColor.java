package net.mostlyoriginal.game.component;

import com.badlogic.gdx.math.MathUtils;

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

    public static ChainColor random() {
        return ChainColor.values()[MathUtils.random(0,ChainColor.values().length-2)];
    }
}
