package content.logic.triggers;

import content.logic.triggers.effects.PrintEffect;
import content.logic.triggers.conditions.RandomChanceCondition;
import content.logic.triggers.effects.AddDialogueEffect;
import content.logic.triggers.engine.ConditionRegistry;
import content.logic.triggers.engine.EffectRegistry;

public final class TriggerGameBindings {

    private static boolean installed = false;

    private TriggerGameBindings() {
    }

    public static synchronized void install() {
        if (installed) {
            return;
        }
        installed = true;

        // CONDITIONS
        ConditionRegistry.register("randomChance", RandomChanceCondition::fromSpec);

        // EFFECTS
        EffectRegistry.register("print", PrintEffect::fromSpec);
        EffectRegistry.register("addDialogue", AddDialogueEffect::fromSpec);

        // EVENTS auto iterated
        for (GameEvent event : GameEvent.values()) {
            TriggerEventRegistry.register(event.toString(), event);
        }
    }
}
