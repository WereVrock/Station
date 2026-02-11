package content.logic.triggers;

import content.logic.triggers.effects.PrintEffect;
import content.logic.triggers.conditions.RandomChanceCondition;
import content.logic.triggers.engine.ConditionRegistry;
import content.logic.triggers.engine.EffectRegistry;

public final class TriggerGameBindings {

    private static boolean installed = false;

    private TriggerGameBindings() {}

    public static synchronized void install() {
        if (installed) return;
        installed = true;

        // CONDITIONS
        ConditionRegistry.register(
                "randomChance",
                RandomChanceCondition::fromSpec
        );

        // EFFECTS
        EffectRegistry.register(
                "print",
                PrintEffect::fromSpec
        );

        // EVENTS
        TriggerEventRegistry.register("VISIT_START", GameEvent.VISIT_START);
        TriggerEventRegistry.register("VISIT_END", GameEvent.VISIT_END);
        TriggerEventRegistry.register("TIMER_START", GameEvent.TIMER_START);
        TriggerEventRegistry.register("DAY_START", GameEvent.DAY_START);
        TriggerEventRegistry.register("DAY_END", GameEvent.DAY_END);
    }
}