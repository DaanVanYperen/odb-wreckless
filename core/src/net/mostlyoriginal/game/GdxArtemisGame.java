package net.mostlyoriginal.game;

import com.badlogic.gdx.Game;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.screen.detection.OdbFeatureScreen;

public class GdxArtemisGame extends Game {

	private static GdxArtemisGame instance;

	@Override
	public void create() {
		instance = this;
		restart();
	}

	public void restart()
	{
		G.level=1;
		setScreen(new GameScreen());
	}

	public static GdxArtemisGame getInstance()
	{
		return instance;
	}
}
