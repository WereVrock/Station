package ui;

import logic.GameEngine;
import main.FireStatus;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final GameEngine engine;
    private final JLabel resources;
    private final JLabel fireStatusLabel;

    public StatusPanel(GameEngine engine) {
        this.engine = engine;

        setLayout(new BorderLayout());

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        resources = new JLabel();
        fireStatusLabel = new JLabel();

        JButton inventory = new JButton("I");
        inventory.addActionListener(e ->
            InventoryDialog.show(engine.getGame())
        );

        topRow.add(resources);
        topRow.add(inventory);

        bottomRow.add(fireStatusLabel);

        add(topRow, BorderLayout.NORTH);
        add(bottomRow, BorderLayout.SOUTH);
    }

    public void refresh() {
        resources.setText(
            "Day: " + engine.getGame().day +
            " | Food: " + engine.getGame().player.food +
            " | Fuel: " + engine.getGame().player.fuel +
            " | Money: " + engine.getGame().player.money
        );

        if (engine.getGame().waitingForBurnChoice) {
            fireStatusLabel.setText("");
            fireStatusLabel.setVisible(false);
            return;
        }

        FireStatus status = engine.getGame().getFireStatus();

        fireStatusLabel.setText(
            "Fire: " +
            status.getStrength().name() +
            " (" + status.getEffect() + ")"
        );
        fireStatusLabel.setVisible(true);
    }
}