package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.Dialog;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class DialogSystem extends FluidIteratingSystem {

    public DialogSystem() {
        super(Aspect.all(net.mostlyoriginal.game.component.Dialog.class));
    }

    public DialogSystem playerSay(Dialog dialog, float delay, float duration) {
        say(entityWithTag("player"), "playerDialog", "cloud-" + dialog.toString().toLowerCase(), delay, duration);
        return this;
    }

    public DialogSystem robotSay(Dialog dialog, float delay, float duration) {
        say(entityWithTag("robot"), "robotDialog", "cloud-" + dialog.toString().toLowerCase(), delay, duration);
        return this;
    }

    private void say(E e, String tag, String animId,  float delay, float duration) {

        E dialog = entityWithTag(tag);
        if (dialog != null && dialog.animId().equals(animId)) return; // don't repeat.
        if (dialog != null) dialog.deleteFromWorld();

        dialog = E.E().pos(e.posX() + e.boundsCx() - 8, e.posY() + e.boundsMaxy() + 6)
                .dialogEntityId(e.id())
                .tag(tag)
                .bounds(0, 0, 16, 16)
                .render(G.LAYER_DIALOGS)
                .anim(animId);

        if ( delay > 0 ) {
            dialog.invisible().script(sequence(delay(delay), remove(Invisible.class), delay(duration), deleteFromWorld()));
        } else {
            dialog.script(sequence(delay(duration), deleteFromWorld()));
        }
    }

    @Override
    protected void process(E e) {
        E follow = E.E(e.dialogEntityId());

        e.posX(follow.posX() + follow.boundsCx() - 8);
        e.posY(follow.posY() + follow.boundsMaxy() + 6);
    }

    public enum Dialog {
        HEART, HAPPY, BATTERY, E, SAD
    }
}
