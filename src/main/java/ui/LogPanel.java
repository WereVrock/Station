// ===== LogPanel.java =====
package ui;

import main.Character;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {

    private final JTextArea area;

    public LogPanel() {
        area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);

        setLayout(new BorderLayout());
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public void dayStart(int day) {
        area.append("\nDay " + day + "\n");
        area.append("What will you burn today?\n");
    }

    public void characterAppears(Character c) {
        area.append(c.name + " appears!\n");
        area.append(c.background + "\n");
    }

    public void noOneAppears() {
        area.append("No one appears.\n");
    }
}
