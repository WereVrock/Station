package content.logic.triggers.effects;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import logic.visit.VisitTriggerContext;
import main.Visit;

public final class AddDialogueEffect implements Effect {

    private final String text;

    private AddDialogueEffect(String text) {
        this.text = text;
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

        visit.dialogue.add(text);
    }

    public static Effect fromSpec(SpecNode node) {

        Object raw = node.args.get("text");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException(
                    "addDialogue requires 'text' string");
        }

        return new AddDialogueEffect((String) raw);
    }

    @Override
    public String toString() {
        return "AddDialogueEffect{text='" + text + "'}";
    }
}