package content.logic.triggers.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ConditionRegistry {

    private ConditionRegistry() {}

    public static void register(String keyword,
                                Function<SpecNode, Condition> builder) {
        instance().map.put(keyword, builder);
    }

    static Function<SpecNode, Condition> get(String keyword) {
        return instance().map.get(keyword);
    }

    private static Registry instance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final Registry INSTANCE = new Registry();
    }

    private static final class Registry {
        private final Map<String, Function<SpecNode, Condition>> map =
                new HashMap<>();
    }
}