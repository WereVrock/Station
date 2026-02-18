package content.logic.triggers.conditions;

import content.logic.triggers.engine.Condition;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import tag.TagManager;

public final class HasTagCondition implements Condition {

    private final String tagName;

    private HasTagCondition(String tagName) {
        if (tagName == null || tagName.isBlank()) {
            throw new IllegalArgumentException("hasTag requires non-empty 'name'");
        }
        this.tagName = tagName;
    }

    @Override
    public boolean isTrue(TriggerContext context) {
        return TagManager.hasByName(tagName);
    }

    public static Condition fromSpec(SpecNode node) {
        Object raw = node.args.get("name");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException(
                    "hasTag requires string 'name'");
        }

        return new HasTagCondition((String) raw);
    }

    @Override
    public String toString() {
        return "HasTagCondition{name=" + tagName + "}";
    }
}