package content.logic.triggers.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class EffectRegistry {

    private static final Map<String, Function<SpecNode, Effect>> registry = new HashMap<>();

    private EffectRegistry() {}

    public static void register(String type, Function<SpecNode, Effect> builder) {
        registry.put(type, builder);
    }

    public static Function<SpecNode, Effect> get(String type) {
        return registry.get(type);
    }
}
