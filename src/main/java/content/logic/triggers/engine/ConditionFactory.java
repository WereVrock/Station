package content.logic.triggers.engine;

public final class ConditionFactory {

    private ConditionFactory() {}

    public static Condition create(SpecNode spec) {

        TriggerEngineBootstrap.ensureInitialized();

        if (spec == null) {
            return null;
        }

        if (spec.type == null) {
            throw new IllegalArgumentException("Condition spec missing type");
        }

        var builder = ConditionRegistry.get(spec.type);

        if (builder == null) {
            throw new IllegalStateException(
                    "No condition registered: " + spec.type);
        }

        return builder.apply(spec);
    }
}