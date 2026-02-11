package content.logic.triggers.engine;

import java.util.List;

public final class TriggerEngine {

    private TriggerEngine() {}

    public static void evaluate(List<Trigger> triggers,
                                String event,
                                TriggerContext context) {

        if (triggers == null || event == null) return;

        for (Trigger trigger : triggers) {

            if (!event.equals(trigger.getEvent())) continue;

            if (trigger.check(context)) {
                trigger.execute(context);
            }
        }
    }
}