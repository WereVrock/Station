package content.logic.triggers;

import content.logic.triggers.engine.ConditionFactory;
import content.logic.triggers.engine.EffectFactory;
import content.logic.triggers.engine.Trigger;
import content.logic.triggers.engine.TriggerSpec;

public final class TriggerFactory {

    private TriggerFactory() {}

    public static Trigger create(TriggerSpec spec) {

        if (spec == null) {
            throw new IllegalArgumentException("TriggerSpec is null");
        }

        GameEvent event = TriggerEventResolver.resolve(spec.event);

        var condition = ConditionFactory.create(spec.condition);
        var effect = EffectFactory.create(spec.effect);

        return new Trigger(event.name(), condition, effect);
    }
}