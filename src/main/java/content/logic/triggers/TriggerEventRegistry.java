package content.logic.triggers;

import java.util.HashMap;
import java.util.Map;

public final class TriggerEventRegistry {

    private TriggerEventRegistry() {}

    private static final Map<String, GameEvent> map = new HashMap<>();

    public static void register(String key, GameEvent event) {
        map.put(key, event);
    }

    public static GameEvent resolve(String key) {
        return map.get(key);
    }
}