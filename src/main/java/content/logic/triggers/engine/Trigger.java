package content.logic.triggers.engine;

import java.util.Objects;

public final class Trigger {

    private final Condition condition;
    private final Effect effect;

    public Trigger(Condition condition, Effect effect) {
        this.condition = Objects.requireNonNull(condition);
        this.effect = Objects.requireNonNull(effect);
    }

    boolean check(TriggerContext context) {
        return condition.isTrue(context);
    }

    void execute(TriggerContext context) {
        effect.apply(context);
    }
}