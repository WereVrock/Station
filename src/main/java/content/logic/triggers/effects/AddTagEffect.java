package content.logic.triggers.effects;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import tag.Tag;
import tag.TagManager;

public final class AddTagEffect implements Effect {

    private final String name;
    private final int expirationDays;

    private AddTagEffect(String name, int expirationDays) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("addTag requires non-empty 'name'");
        }
        this.name = name;
        this.expirationDays = expirationDays;
    }

    @Override
    public void apply(TriggerContext context) {
        Tag tag = (expirationDays < 0)
                ? new Tag(name)
                : new Tag(name, expirationDays);

        TagManager.add(tag);
    }

    public static Effect fromSpec(SpecNode node) {
        Object rawName = node.args.get("name");
        if (!(rawName instanceof String)) {
            throw new IllegalArgumentException("addTag requires string 'name'");
        }

        Object rawExpiration = node.args.get("expirationDays");
        int expiration = -1;

        if (rawExpiration != null) {
            if (!(rawExpiration instanceof Number)) {
                throw new IllegalArgumentException("addTag 'expirationDays' must be a number");
            }
            expiration = ((Number) rawExpiration).intValue();
        }

        return new AddTagEffect((String) rawName, expiration);
    }

    @Override
    public String toString() {
        return "AddTagEffect{name='" + name + "', expirationDays=" + expirationDays + "}";
    }
}