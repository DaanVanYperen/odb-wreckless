package net.mostlyoriginal.game.client;

/**
 * @author Daan van Yperen
 */
public interface SfxHandler {
    void play(String sfx);
    void play(String sfx, float volumeFactor);

    void playDelayed(String sfx, float delay);
}
