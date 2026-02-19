package content.logic.triggers.conditions;

import content.logic.triggers.engine.Condition;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import content.logic.triggers.engine.ConditionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class AndCondition implements Condition {

    private final List<Condition> conditions;

    private AndCondition(List<Condition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException("and requires at least one condition");
        }
        this.conditions = conditions;
    }

    @Override
    public boolean isTrue(TriggerContext context) {
        for (Condition c : conditions) {
            if (!c.isTrue(context)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static Condition fromSpec(SpecNode node) {

        if (node.args == null) {
            throw new IllegalArgumentException("and requires 'conditions'");
        }

        Object raw = node.args.get("conditions");

        if (!(raw instanceof List)) {
            throw new IllegalArgumentException("and requires list 'conditions'");
        }

        List<?> rawList = (List<?>) raw;

        if (rawList.isEmpty()) {
            throw new IllegalArgumentException("and requires at least one nested condition");
        }

        List<Condition> built = new ArrayList<>();

        for (Object entry : rawList) {

            SpecNode nestedSpec;

            if (entry instanceof SpecNode) {
                nestedSpec = (SpecNode) entry;
            }
            else if (entry instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) entry;

                nestedSpec = new SpecNode();
                nestedSpec.type = (String) map.get("type");

                Object args = map.get("args");
                if (args instanceof Map) {
                    nestedSpec.args = (Map<String, Object>) args;
                }
                else {
                    throw new IllegalArgumentException("Invalid nested args in 'and'");
                }
            }
            else {
                throw new IllegalArgumentException("Invalid nested condition in 'and'");
            }

            built.add(ConditionFactory.create(nestedSpec));
        }

        return new AndCondition(built);
    }

    @Override
    public String toString() {
        return "AndCondition{conditions=" + conditions + "}";
    }
}
