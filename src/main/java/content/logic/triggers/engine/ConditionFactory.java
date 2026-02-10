package content.logic.triggers.engine;

import java.util.function.Function;

public class ConditionFactory {

    public static Condition create(SpecNode spec) {
        if (spec == null || spec.type == null) {
            throw new IllegalArgumentException("Condition spec missing type");
        }

        Function<SpecNode, Condition> builder =
                ConditionRegistry.get(spec.type);

        if (builder == null) {
            throw new IllegalStateException("No condition registered: " + spec.type);
        }

        return builder.apply(spec);
    }
}
