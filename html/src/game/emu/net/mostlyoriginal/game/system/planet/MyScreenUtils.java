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

    private static ImageData imageData;

    public static void putPixelsBack(Pixmap pixmap, ByteBuffer pixels) {
        if (pixmap.getWidth() == 0 || pixmap.getHeight() == 0) return;
        if (imageData == null) {
            imageData = pixmap.getContext().createImageData(pixmap.getHeight(), pixmap.getWidth());
        }
        putPixelsBack(pixels.array(), pixmap.getWidth(), pixmap.getHeight(), pixmap.getContext(), imageData);
    }

    private native static void putPixelsBack(byte[] pixels, int width, int height, Context2d ctx, ImageData imgData)/*-{
        var buf = new ArrayBuffer(imgData.data.length);
        var target = new Uint8ClampedArray(buf);
        for (var i = 0, len = width * height * 4; i < len; i++) {
            target[i] = pixels[i] & 0xff;
        }
        imgData.data.set(target);
        ctx.putImageData(imgData, 0, 0);
    }-*/;

}
