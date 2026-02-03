package ui;

import logic.GameEngine;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final GameEngine engine;
    private final JLabel resources;

    public StatusPanel(GameEngine engine) {
        this.engine = engine;
        this.resources = new JLabel();

        setLayout(new BorderLayout());

        JButton inventory = new JButton("I");
        inventory.addActionListener(e ->
            InventoryDialog.show(engine.getGame())
        );

        add(resources, BorderLayout.CENTER);
        add(inventory, BorderLayout.EAST);
    }

    public void refresh() {
        resources.setText(
            "Day: " + engine.getGame().day +
            " | Food: " + engine.getGame().player.food +
            " | Fuel: " + engine.getGame().player.fuel +
            " | Money: " + engine.getGame().player.money
        );
    }
}
