package content.logic.triggers.conditions;

import content.logic.triggers.engine.Condition;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import content.logic.triggers.engine.ConditionFactory;

import java.util.Map;

public final class NotCondition implements Condition {

    private final Condition inner;

    private NotCondition(Condition inner) {
        if (inner == null) {
            throw new IllegalArgumentException("NotCondition requires inner condition");
        }
        this.inner = inner;
    }

    @Override
    public boolean isTrue(TriggerContext context) {
        return !inner.isTrue(context);
    }

    @SuppressWarnings("unchecked")
    public static Condition fromSpec(SpecNode node) {

        if (node.args == null) {
            throw new IllegalArgumentException("not requires 'condition' argument");
        }

        Object raw = node.args.get("condition");

        if (raw == null) {
            throw new IllegalArgumentException("not requires nested 'condition'");
        }

        SpecNode innerSpec;

        if (raw instanceof SpecNode) {
            innerSpec = (SpecNode) raw;
        }
        else if (raw instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) raw;

            innerSpec = new SpecNode();
            innerSpec.type = (String) map.get("type");

            Object args = map.get("args");
            if (args instanceof Map) {
                innerSpec.args = (Map<String, Object>) args;
            } else {
                throw new IllegalArgumentException(
                        "Invalid nested condition args in 'not'");
            }
        }
        else {
            throw new IllegalArgumentException(
                    "not requires nested condition object");
        }

        Condition inner = ConditionFactory.create(innerSpec);

        return new NotCondition(inner);
    }

    @Override
    public String toString() {
        return "NotCondition{inner=" + inner + "}";
    }
}
