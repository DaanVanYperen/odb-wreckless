package net.mostlyoriginal.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.component.G;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(G.SCREEN_WIDTH, G.SCREEN_HEIGHT);
        return configuration;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new GdxArtemisGame();
    }

    @Override
    public Preloader.PreloaderCallback getPreloaderCallback() {
        final VerticalPanel preloaderPanel = new VerticalPanel();
        preloaderPanel.setStyleName("gdx-preloader");
        preloaderPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        final Image logo = new Image(GWT.getModuleBaseURL() + "logo.gif");
        logo.setStyleName("logo");
        preloaderPanel.add(logo);
        final Panel meterPanel = new SimplePanel();
        meterPanel.setStyleName("gdx-meter");
        meterPanel.addStyleName("red");
        final InlineHTML meter = new InlineHTML();
        final Style meterStyle = meter.getElement().getStyle();
        meterStyle.setWidth(0, Style.Unit.PCT);
        meterPanel.add(meter);
        preloaderPanel.add(meterPanel);
        getRootPanel().add(preloaderPanel);
        return new Preloader.PreloaderCallback() {
            @Override
            public void error(String file) {
                System.out.println("error: " + file);
            }

            @Override
            public void update(Preloader.PreloaderState state) {
                meterStyle.setWidth(100f * state.getProgress(), Style.Unit.PCT);
            }

        };
    }
}