package net.mostlyoriginal.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Settings;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.screen.detection.OdbFeatureScreen;

public class GdxArtemisGame extends Game {

    private static GdxArtemisGame instance;

    @Override
    public void create() {
        instance = this;
        G.settings = (new Json()).fromJson(Settings.class, Gdx.files.internal("settings.json"));

        restart();
    }

    public void restart() {
        G.level = G.settings.startingLevel;
        setScreen(new GameScreen());
    }

    public static GdxArtemisGame getInstance() {
        return instance;
    }
}
