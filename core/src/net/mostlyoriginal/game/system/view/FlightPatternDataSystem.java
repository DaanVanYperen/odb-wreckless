package net.mostlyoriginal.game.system.view;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.game.component.FlightPatternData;
import net.mostlyoriginal.game.component.ShipData;
import net.mostlyoriginal.game.system.render.FlightPatternLibrary;
import net.mostlyoriginal.game.system.render.ShipLibrary;

/**
 * @author Daan van Yperen
 */
@Wire
public class FlightPatternDataSystem extends BaseSystem {

    private FlightPatternLibrary library;

    public FlightPatternDataSystem() {
        super();
        load();
    }

    @Override
    protected void processSystem() {
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    private void load() {
        final Json json = new Json();
        library = json.fromJson(FlightPatternLibrary.class, Gdx.files.internal("flightpatterns.json"));
    }

    public FlightPatternData get(String id ) {
        return library.getById(id);
    }
}
