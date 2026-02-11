package content.logic.triggers;

import content.logic.triggers.effects.PrintEffect;
import content.logic.triggers.conditions.RandomChanceCondition;
import content.logic.triggers.engine.ConditionRegistry;
import content.logic.triggers.engine.EffectRegistry;

/**
 * Game-side trigger wiring.
 * This is where ALL condition/effect implementations are registered.
 */
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
    }
}
