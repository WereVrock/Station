package content.logic.triggers;

import content.logic.triggers.conditions.HasTagCondition;
import content.logic.triggers.effects.PrintEffect;
import content.logic.triggers.effects.PrintDialogueEffect;
import content.logic.triggers.conditions.RandomChanceCondition;
import content.logic.triggers.conditions.NotCondition;
import content.logic.triggers.effects.AddDialogueEffect;
import content.logic.triggers.effects.AddTagEffect;
import content.logic.triggers.effects.RemoveTagEffect;
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
        ConditionRegistry.register("not", NotCondition::fromSpec);
        ConditionRegistry.register("hasTag", HasTagCondition::fromSpec);

        // EFFECTS
        EffectRegistry.register("print", PrintEffect::fromSpec);
        EffectRegistry.register("addDialogue", AddDialogueEffect::fromSpec);
        EffectRegistry.register("printDialogue", PrintDialogueEffect::fromSpec);
        EffectRegistry.register("addTag", AddTagEffect::fromSpec);
        EffectRegistry.register("removeTag", RemoveTagEffect::fromSpec);

        // EVENTS auto iterated
        for (GameEvent event : GameEvent.values()) {
            TriggerEventRegistry.register(event.toString(), event);
        }
    }
}
