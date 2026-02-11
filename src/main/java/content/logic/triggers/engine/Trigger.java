package content.logic.triggers.engine;

import java.util.Objects;

public final class Trigger {

    private final Enum<?> event;
    private final Condition condition;
    private final Effect effect;

    public Trigger(Enum<?> event,
                   Condition condition,
                   Effect effect) {

        this.event = Objects.requireNonNull(event);
        this.condition = Objects.requireNonNull(condition);
        this.effect = Objects.requireNonNull(effect);
    }

    Enum<?> getEvent() {
        return event;
    }

    boolean check(TriggerContext context) {
        return condition.isTrue(context);
    }

    void execute(TriggerContext context) {
        effect.apply(context);
    }
}
