package content.logic.triggers.engine;

import java.util.ArrayList;
import java.util.List;

public class TriggerEngine {

    private final List<Trigger> triggers = new ArrayList<>();

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void clear() {
        triggers.clear();
    }

    public void evaluate(TriggerContext context) {
        for (Trigger trigger : triggers) {
            if (trigger.check(context)) {
                trigger.execute(context);
            }
        }
    }
}
