package content.logic.triggers.effects;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;

public final class PrintEffect implements Effect {

    private final String text;

    private PrintEffect(String text) {
        this.text = text;
    }

    @Override
    public void apply(TriggerContext context) {
        System.out.println(text);
    }

    public static Effect fromSpec(SpecNode node) {
        Object raw = node.args.get("text");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException(
                    "print requires 'text' string");
        }

        return new PrintEffect((String) raw);
    }

    @Override
    public String toString() {
        return "PrintEffect{message='" + text + "'}";
    }
}
