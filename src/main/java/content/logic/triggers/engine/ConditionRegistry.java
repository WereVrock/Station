package content.logic.triggers.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ConditionRegistry {

    private static final Map<String, Function<SpecNode, Condition>> registry = new HashMap<>();

    private ConditionRegistry() {}

    public static void register(String type, Function<SpecNode, Condition> builder) {
        registry.put(type, builder);
    }

    public static Function<SpecNode, Condition> get(String type) {
        return registry.get(type);
    }
}
