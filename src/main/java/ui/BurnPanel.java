package ui;

import logic.GameEngine;
import logic.visit.VisitResult;
import main.ItemStack;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class BurnPanel extends JPanel {

    private final GameEngine engine;
    private final Consumer<List<VisitResult>> onBurnVisits;

    public BurnPanel(GameEngine engine, Consumer<List<VisitResult>> onBurnVisits) {
        this.engine = engine;
        this.onBurnVisits = onBurnVisits;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void refresh() {
        removeAll();

        if (!engine.getGame().waitingForBurnChoice) {
            revalidate();
            repaint();
            return;
        }

        add(Box.createVerticalGlue());

        if (engine.getGame().player.fuel > 0) {
            JButton fuel = new JButton("Burn Fuel");
            fuel.setAlignmentX(Component.CENTER_ALIGNMENT);
            fuel.addActionListener(e ->
                onBurnVisits.accept(engine.burnFuelVisits())
            );
            add(fuel);
            add(Box.createVerticalStrut(10));
        }

        for (ItemStack stack : engine.getGame().player.inventory) {
            if (!stack.item.burnable) continue;

            String label = stack.count > 1
                    ? stack.item.name + " x" + stack.count
                    : stack.item.name;

            JButton btn = new JButton("Burn " + label);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(e ->
                onBurnVisits.accept(engine.burnItemVisits(stack.item))
            );

            add(btn);
            add(Box.createVerticalStrut(5));
        }

        add(Box.createVerticalGlue());
        revalidate();
        repaint();
    }
}