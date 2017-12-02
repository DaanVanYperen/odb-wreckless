package net.mostlyoriginal.game.system.view;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.game.component.ArsenalData;
import net.mostlyoriginal.game.system.render.ArsenalLibrary;

/**
 * @author Daan van Yperen
 */
@Wire
public class ArsenalDataSystem extends BaseSystem {

    private ArsenalLibrary arsenalLibrary;

    public ArsenalDataSystem() {
        super();
        loadArsenals();
    }

    @Override
    protected void processSystem() {
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    private void loadArsenals() {
        final Json json = new Json();
        arsenalLibrary = json.fromJson(ArsenalLibrary.class, Gdx.files.internal("arsenal.json"));
    }

    public ArsenalData get( String id ) {
        return arsenalLibrary.getById(id);
    }
}
