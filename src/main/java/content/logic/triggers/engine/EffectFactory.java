package content.logic.triggers.engine;

import java.util.function.Function;

public class EffectFactory {

    public static Effect create(SpecNode spec) {
        if (spec == null || spec.type == null) {
            throw new IllegalArgumentException("Effect spec missing type");
        }

        Function<SpecNode, Effect> builder =
                EffectRegistry.get(spec.type);

        if (builder == null) {
            throw new IllegalStateException("No effect registered: " + spec.type);
        }

        return builder.apply(spec);
    }
}
