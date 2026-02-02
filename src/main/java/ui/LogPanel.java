package ui;

import logic.VisitResult;
import main.GameCharacter;
import main.Item;

import javax.swing.*;
import java.util.List;

public class LogPanel extends JPanel {

    private final JTextArea area;

    public LogPanel() {
        area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);

        setLayout(new java.awt.BorderLayout());
        add(new JScrollPane(area), java.awt.BorderLayout.CENTER);
    }

    public void dayStart(int day) {
        area.append("\nDay " + day + "\n");
        area.append("What will you burn today?\n");
    }

    public void noOneAppears() {
        area.append("No one appears.\n");
    }

    public void visitAppears(VisitResult visit) {
        GameCharacter c = visit.character;
        area.append("\n" + c.name + " appears!\n");
        if (c.background != null) area.append(c.background + "\n");

        if (!visit.itemsForSale.isEmpty()) {
            area.append("Items for sale:\n");
            for (Item item : visit.itemsForSale) {
                area.append(" - " + item.name + " (" + item.type + ")\n");
            }
        }

        if (!visit.dialogue.isEmpty()) {
            area.append("Dialogue:\n");
            for (String line : visit.dialogue) {
                area.append(c.name + ": " + line + "\n");
            }
        }

        area.append("\n");
    }
}
