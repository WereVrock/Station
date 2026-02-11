package content.logic.triggers.engine;

public final class TriggerCompiler {

    private TriggerCompiler() {}

    public static Trigger compile(TriggerSpec spec) {

        if (spec == null) {
            throw new IllegalArgumentException("TriggerSpec is null");
        }

        if (spec.event == null) {
            throw new IllegalArgumentException("TriggerSpec missing event");
        }

        var condition = ConditionFactory.create(spec.condition);
        var effect = EffectFactory.create(spec.effect);

        return new Trigger(spec.event, condition, effect);
    }
}