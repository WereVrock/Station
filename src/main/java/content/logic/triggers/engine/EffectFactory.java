package content.logic.triggers.engine;

public final class EffectFactory {

    private EffectFactory() {}

    public static Effect create(SpecNode spec) {

        TriggerEngineBootstrap.ensureInitialized();

        if (spec == null || spec.type == null) {
            throw new IllegalArgumentException("Effect spec missing type");
        }

        var builder = EffectRegistry.get(spec.type);

        if (builder == null) {
            throw new IllegalStateException(
                    "No effect registered: " + spec.type);
        }

        return builder.apply(spec);
    }
}