package content.logic.triggers.engine;

import java.util.List;

public final class TriggerEngine {

    private TriggerEngine() {}

    public static void evaluate(List<Trigger> triggers,
                                TriggerContext context) {

        for (Trigger trigger : triggers) {
            if (trigger.check(context)) {
                trigger.execute(context);
            }
        }
    }
}