package net.mostlyoriginal.game.system.planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * @author Daan van Yperen
 */
public class FauxPixMap {
    public static final float MAX_DEVIATION = 0.2f;
    private final Pixmap pixmap;

//    private ByteBuffer byteBuffer;
//    private int width = 0;
//    private int height = 0;
//    private byte[] a;

    public FauxPixMap(String texture) {
        pixmap = new Pixmap(Gdx.files.internal(texture));
//        PNGDecoder pngDecoder = null;
//        try {
//            pngDecoder = new PNGDecoder(Gdx.files.internal(texture).read());
//            byteBuffer = ByteBuffer.allocate(4 * pngDecoder.getWidth() * pngDecoder.getHeight());
//            this.width = pngDecoder.getWidth();
//            this.height = pngDecoder.getHeight();
//            pngDecoder.decode(byteBuffer, pngDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
//            a = byteBuffer.array();
//        } catch (IOException e) {
//        }
    }

    public int getPixel(int x, int y) {
        return pixmap.getPixel(x, y);
//        byte r = a[(x + y * width) * 4];
//        byte g = a[(x + y * width) * 4 + 1];
//        byte b = a[(x + y * width) * 4 + 2];
//        byte aa = a[(x + y * width) * 4 + 3];
//        int color = ((int) (r) << 24) | ((int) (g) << 16) | ((int) (b) << 8) | (int) (aa);
//        Color ccc = new Color();
//        Color.rgba8888ToColor(ccc, color);
//        System.out.println(ccc.toString());
    }

    public void dispose() {
        pixmap.dispose();
    }

    public static boolean sameIsh(int color, int intColor) {
        float r = ((color & 0xff000000) >>> 24) / 255f;
        float g = ((color & 0x00ff0000) >>> 16) / 255f;
        float b = ((color & 0x0000ff00) >>> 8) / 255f;
        float a = ((color & 0x000000ff)) / 255f;

        float r2 = ((intColor & 0xff000000) >>> 24) / 255f;
        float g2 = ((intColor & 0x00ff0000) >>> 16) / 255f;
        float b2 = ((intColor & 0x0000ff00) >>> 8) / 255f;
        float a2 = ((intColor & 0x000000ff)) / 255f;

        return near(r2, r, MAX_DEVIATION) && near(g2, g, MAX_DEVIATION) && near(b2, b, MAX_DEVIATION);
    }

    private static boolean near(float r2, float r, float deviation) {
        return (r2 >= r - deviation) && (r2 <= r + deviation);
    }
}
