package net.mostlyoriginal.game.system.planet;

import com.badlogic.gdx.graphics.Pixmap;
import java.nio.ByteBuffer;
import java.nio.HasArrayBufferView;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;

/**
 * @author Daan van Yperen
 */
public class MyScreenUtils {

    public static void putPixelsBack (Pixmap pixmap, ByteBuffer pixels) {
        if (pixmap.getWidth() == 0 || pixmap.getHeight() == 0) return;
        putPixelsBack(pixels.array(), pixmap.getWidth(), pixmap.getHeight(), pixmap.getContext());
    }

    private native static void putPixelsBack (byte[] pixels, int width, int height, Context2d ctx)/*-{
        var imgData = ctx.createImageData(width, height);
        var data = imgData.data;
        for (var i = 0, len = width * height * 4; i < len; i++) {
            data[i] = pixels[i] & 0xff;
        }
        ctx.putImageData(imgData, 0, 0);
    }-*/;

}
