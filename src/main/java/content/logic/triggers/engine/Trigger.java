package content.logic.triggers.engine;

import java.util.Objects;

public class Trigger {

    private final Condition condition;
    private final Effect effect;

    public Trigger(Condition condition, Effect effect) {
        this.condition = Objects.requireNonNull(condition);
        this.effect = Objects.requireNonNull(effect);
    }

    public boolean check(TriggerContext context) {
        return condition.isTrue(context);
    }

    public void execute(TriggerContext context) {
        effect.apply(context);
    }

    public Condition getCondition() {
        return condition;
    }

    public Effect getEffect() {
        return effect;
    }
}
