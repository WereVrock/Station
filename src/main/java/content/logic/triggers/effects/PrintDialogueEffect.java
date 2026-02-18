package content.logic.triggers.effects;

import content.logic.triggers.engine.Effect;
import content.logic.triggers.engine.SpecNode;
import content.logic.triggers.engine.TriggerContext;
import ui.MainDisplayPanel;

public final class PrintDialogueEffect implements Effect {

    private final String line;

    private PrintDialogueEffect(String line) {
        this.line = line;
    }

    @Override
    public void apply(TriggerContext context) {
//        System.out.println("apply");
        MainDisplayPanel.appendToDialogueStatic(line);
    }

    public static Effect fromSpec(SpecNode node) {
        Object raw = node.args.get("line");

        if (!(raw instanceof String)) {
            throw new IllegalArgumentException(
                    "printDialogue requires 'line' string");
        }

        return new PrintDialogueEffect((String) raw);
    }

    @Override
    public String toString() {
        return "PrintDialogueEffect{line='" + line + "'}";
    }
}