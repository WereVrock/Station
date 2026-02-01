// ===== BurnPanel.java =====
package ui;

import logic.GameEngine;
import main.Item;
import main.Character;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

public class BurnPanel extends JPanel {

    private final GameEngine engine;
    private final Consumer<Optional<Character>> onBurn;

    public BurnPanel(GameEngine engine, Consumer<Optional<Character>> onBurn) {
        this.engine = engine;
        this.onBurn = onBurn;

        setLayout(new GridLayout(0, 4, 5, 5));
    }

    public void refresh() {
        removeAll();

        if (!engine.getGame().waitingForBurnChoice) {
            revalidate();
            repaint();
            return;
        }

        if (engine.getGame().player.fuel > 0) {
            JButton fuel = new JButton("Burn Fuel");
            fuel.addActionListener(e -> onBurn.accept(engine.burnFuel()));
            add(fuel);
        }

        for (Item item : engine.getGame().player.inventory) {
            if (!item.burnable) continue;

            JButton btn = new JButton(item.name);
            btn.addActionListener(e -> onBurn.accept(engine.burnItem(item)));
            add(btn);
        }

        revalidate();
        repaint();
    }
}
