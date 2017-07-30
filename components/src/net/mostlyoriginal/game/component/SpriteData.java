
package net.mostlyoriginal.game.component;

import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class SpriteData implements Serializable {

    public String id;
    public String comment; // not used.

    public int x;
    public int y;
    public int width;
    public int height;
    public int countX = 1;
    public int countY = 1;
    public float milliseconds = 200;
    public boolean repeat = true;

    public SpriteData() {
    }
}
