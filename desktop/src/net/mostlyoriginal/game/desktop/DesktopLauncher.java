package net.mostlyoriginal.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.component.G;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = G.SCREEN_WIDTH;
		config.height = G.SCREEN_HEIGHT;
		config.title = "Little Fortune Planet Sandbox - LD38 Post Compo";
		new LwjglApplication(new GdxArtemisGame(), config);
	}
}
