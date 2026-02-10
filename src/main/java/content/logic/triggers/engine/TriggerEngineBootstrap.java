package content.logic.triggers.engine;

/**
 * Hidden, lazy, one-time initializer.
 * Never exposed outside this package.
 */
final class TriggerEngineBootstrap {

    private static boolean initialized = false;

    private TriggerEngineBootstrap() {}

    static synchronized void ensureInitialized() {
        if (initialized) return;
        initialized = true;

        // Built-in registrations go here
        BuiltinConditions.register();
        BuiltinEffects.register();
    }
}