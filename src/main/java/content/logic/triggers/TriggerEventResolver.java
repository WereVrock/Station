package content.logic.triggers;

public final class TriggerEventResolver {

    private TriggerEventResolver() {}

    public static GameEvent resolve(String raw) {

        if (raw == null) {
            throw new IllegalArgumentException("Trigger event is null");
        }

        GameEvent ev = TriggerEventRegistry.resolve(raw);

        if (ev == null) {
            throw new IllegalStateException(
                    "Unknown trigger event: " + raw);
        }

        return ev;
    }
}