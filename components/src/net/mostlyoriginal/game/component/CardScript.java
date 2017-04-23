package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class CardScript extends Component {
    public CardScript() {
    }
    public CardScript(ScriptCommand[] script) {
        this.script = script;
    }

    public ScriptCommand[] script;
}
