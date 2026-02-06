package ui;

import logic.visit.VisitResult;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {

    private final JTextArea area;

    public LogPanel() {
        area = new JTextArea(4, 20);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        setLayout(new BorderLayout());
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    // ---- Existing API (kept intentionally, behavior simplified) ----

    public void dayStart(int day) {
        log("Day " + day + " begins.");
    }

    public void visitAppears(VisitResult visit) {
        log(visit.character.name + " appears.");
    }

    public void noOneAppears() {
        log("No one appears.");
    }

    // ---- New internal logging ----

    public void log(String text) {
        area.append(text + "\n");
    }

    public void clear() {
        area.setText("");
    }
}
