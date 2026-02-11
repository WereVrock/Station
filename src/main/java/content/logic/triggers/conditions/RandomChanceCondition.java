package content.logic.triggers.conditions;

import content.logic.triggers.engine.Condition;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;

import java.util.Random;

public final class RandomChanceCondition implements Condition {

    private static final Random RNG = new Random();

    private final double chance;

    private RandomChanceCondition(double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("chance must be 0..1");
        }
        this.chance = chance;
    }

    @Override
    public boolean isTrue(TriggerContext context) {
        return RNG.nextDouble() < chance;
    }

    public static Condition fromSpec(SpecNode node) {
        Object raw = node.args.get("chance");

        if (!(raw instanceof Number)) {
            throw new IllegalArgumentException(
                    "randomChance requires numeric 'chance'");
        }

        return new RandomChanceCondition(
                ((Number) raw).doubleValue());
    }

    @Override
    public String toString() {
        return "RandomChanceCondition{chance=" + chance + "}";
    }
}
