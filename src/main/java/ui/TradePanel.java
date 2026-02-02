// ===== TradePanel.java =====
package ui;

import logic.GameEngine;
import main.GameCharacter;
import main.Item;

import javax.swing.*;
import java.awt.*;

public class TradePanel extends JPanel {

    private final GameEngine engine;
    private final Runnable onEndDay;

    private GameCharacter currentCharacter;

    public TradePanel(GameEngine engine, Runnable onEndDay) {
        this.engine = engine;
        this.onEndDay = onEndDay;
        setLayout(new GridLayout(0, 2, 5, 5));
    }

    public void clear() {
        removeAll();
        currentCharacter = null;
        revalidate();
        repaint();
    }

    public void showTrade(GameCharacter character) {
        this.currentCharacter = character;
        removeAll();

        // BUY: items character owns
        for (Item item : character.inventory) {
            JButton buy = new JButton("Buy " + item.name);
            buy.addActionListener(e -> {
                engine.buy(character, item);
                showTrade(character);
            });
            add(buy);
            add(new JLabel("")); // layout balance
        }

        // SELL: items player owns
        for (Item item : engine.getGame().player.inventory) {
            JButton sell = new JButton("Sell " + item.name);
            sell.addActionListener(e -> {
                engine.sell(character, item);
                showTrade(character);
            });
            add(new JLabel(""));
            add(sell);
        }

        addEndDayButton();
        refresh();
    }

    public void showEndDayOnly() {
        removeAll();
        addEndDayButton();
        refresh();
    }

    private void addEndDayButton() {
        JButton endDay = new JButton("End Day");
        endDay.addActionListener(e -> onEndDay.run());
        add(endDay);
    }

    private void refresh() {
        revalidate();
        repaint();
    }
}
