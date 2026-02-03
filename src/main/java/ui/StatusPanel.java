package ui;

import logic.GameEngine;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final GameEngine engine;
    private final JLabel resources;

    public StatusPanel(GameEngine engine) {
        this.engine = engine;

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        resources = new JLabel();

        JButton inventory = new JButton("I");
        inventory.addActionListener(e ->
            InventoryDialog.show(engine.getGame())
        );

        add(resources);
        add(inventory);
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
