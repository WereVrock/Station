package content.logic.triggers.effects;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import tag.TagManager;

public final class RemoveTagEffect implements Effect {

    private final String name;

    private RemoveTagEffect(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("removeTag requires non-empty 'name'");
        }
        this.name = name;
    }

    @Override
    public void apply(TriggerContext context) {
        TagManager.removeByName(name);
    }

    public static Effect fromSpec(SpecNode node) {
        Object raw = node.args.get("name");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException("removeTag requires string 'name'");
        }

        return new RemoveTagEffect((String) raw);
    }

    @Override
    public String toString() {
        return "RemoveTagEffect{name='" + name + "'}";
    }
}