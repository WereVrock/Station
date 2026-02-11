package content.logic.triggers;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;

public final class PrintEffect implements Effect {

    private final String message;

    private PrintEffect(String message) {
        this.message = message;
    }

    @Override
    public void apply(TriggerContext context) {
        System.out.println(message);
    }

    public static Effect fromSpec(SpecNode node) {
        Object raw = node.args.get("message");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException(
                    "print requires 'message' string");
        }

        return new PrintEffect((String) raw);
    }

    @Override
    public String toString() {
        return "PrintEffect{message='" + message + "'}";
    }
}
