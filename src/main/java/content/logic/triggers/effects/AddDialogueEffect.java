package content.logic.triggers.effects;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import logic.visit.VisitTriggerContext;
import main.Visit;

public final class AddDialogueEffect implements Effect {

    private final String message;

    private AddDialogueEffect(String message) {
        this.message = message;
    }

    @Override
    public void apply(TriggerContext context) {

        if (!(context instanceof VisitTriggerContext)) {
            throw new IllegalStateException(
                    "AddDialogueEffect requires VisitTriggerContext");
        }

        VisitTriggerContext visitContext =
                (VisitTriggerContext) context;

        Visit visit = visitContext.getVisit();

        if (visit.dialogue == null) {
            throw new IllegalStateException(
                    "Visit dialogue list is null");
        }

        visit.dialogue.add(message);
    }

    public static Effect fromSpec(SpecNode node) {

        Object raw = node.args.get("message");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException(
                    "addDialogue requires 'message' string");
        }

        return new AddDialogueEffect((String) raw);
    }

    @Override
    public String toString() {
        return "AddDialogueEffect{message='" + message + "'}";
    }
}
