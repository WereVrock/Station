package content.logic.triggers.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class EffectRegistry {

    private EffectRegistry() {}

    public static void register(String keyword,
                                Function<SpecNode, Effect> builder) {
        instance().map.put(keyword, builder);
    }

    static Function<SpecNode, Effect> get(String keyword) {
        return instance().map.get(keyword);
    }

    private static Registry instance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final Registry INSTANCE = new Registry();
    }

    private static final class Registry {
        private final Map<String, Function<SpecNode, Effect>> map =
                new HashMap<>();
    }
}