package content.logic.triggers.engine;

import java.util.List;

public final class TriggerEngine {

    private TriggerEngine() {}

    public static void evaluate(List<Trigger> triggers,
                                Enum<?> event,
                                TriggerContext context) {

        if (triggers == null || event == null) return;

        for (Trigger trigger : triggers) {

            if (trigger.getEvent() != event) continue;

            if (trigger.check(context)) {
                trigger.execute(context);
            }
        }
    }
}
