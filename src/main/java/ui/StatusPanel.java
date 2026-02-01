// ===== StatusPanel.java =====
package ui;

import logic.GameEngine;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final GameEngine engine;
    private final JLabel label;

    public StatusPanel(GameEngine engine) {
        this.engine = engine;
        this.label = new JLabel("", JLabel.CENTER);

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
    }

    public void refresh() {
        label.setText(
            "<html>" +
                "Day: " + engine.getGame().day + "<br>" +
                "Food: " + engine.getGame().player.food + "<br>" +
                "Fuel: " + engine.getGame().player.fuel + "<br>" +
                "Money: " + engine.getGame().player.money +
            "</html>"
        );
    }
}
