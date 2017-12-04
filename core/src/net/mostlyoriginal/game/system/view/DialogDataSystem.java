package net.mostlyoriginal.game.system.view;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.game.component.ArsenalData;
import net.mostlyoriginal.game.component.DialogData;
import net.mostlyoriginal.game.system.render.ArsenalLibrary;
import net.mostlyoriginal.game.system.render.DialogLibrary;

/**
 * @author Daan van Yperen
 */
@Wire
public class DialogDataSystem extends BaseSystem {

    private DialogLibrary library;

    public DialogDataSystem() {
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
        library = json.fromJson(DialogLibrary.class, Gdx.files.internal("dialog.json"));
    }

    public DialogData get(String id ) {
        return library.getById(id);
    }
}
