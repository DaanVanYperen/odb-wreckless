package net.mostlyoriginal.game.system.planet;

import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;

import static net.mostlyoriginal.game.component.G.SIMULATION_HEIGHT;
import static net.mostlyoriginal.game.component.G.SIMULATION_WIDTH;

/**
 * @author Daan van Yperen
 */
public class MyScreenUtils {
    public static void putPixelsBack(Pixmap pixmap, ByteBuffer pixels) {
        pixmap.getPixels().rewind();
        pixmap.getPixels().put(pixels);
        pixmap.getPixels().rewind();
    }
}
