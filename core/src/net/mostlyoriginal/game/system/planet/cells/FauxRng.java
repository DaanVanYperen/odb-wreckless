package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

/**
 * @author Daan van Yperen
 */
public class FauxRng {

    private static final int PREGEN_COUNT = 10000;
    private static int[] pregen = new int[PREGEN_COUNT];
    private static int cursor = 0;

    static {
        Random random = new Random();
        for (int i = 0; i < PREGEN_COUNT; i++) {
            pregen[i] = random.nextInt(Integer.MAX_VALUE);
        }
    }

    /**
     * Returns a random number between 0 and end (inclusive).
     */
    public static int random(int range) {
        return nextInt(range + 1);
    }

    private static int nextInt(int i) {
        cursor = (cursor + 1) % PREGEN_COUNT;
        return pregen[cursor] % i;
    }

    /**
     * Returns a random number between start (inclusive) and end (inclusive).
     */
    static public int random(int start, int end) {
        return start + nextInt(end - start + 1);
    }

    public static boolean randomBoolean() {
        return nextInt(2) == 1;
    }
}
