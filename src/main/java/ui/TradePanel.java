// ===== TradePanel.java =====
package ui;

import logic.GameEngine;
import main.Character;
import main.Item;

import javax.swing.*;
import java.awt.*;

public class TradePanel extends JPanel {

    private final GameEngine engine;
    private final Runnable onEndDay;

    public TradePanel(GameEngine engine, Runnable onEndDay) {
        this.engine = engine;
        this.onEndDay = onEndDay;

        setLayout(new GridLayout(0, 2, 5, 5));
    }

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }

    public void showTrade(Character c) {
        removeAll();

        for (Item item : engine.getGame().items) {
            JButton buy = new JButton("Buy " + item.name);
            buy.addActionListener(e -> engine.buy(item));

            JButton sell = new JButton("Sell " + item.name);
            sell.addActionListener(e -> engine.sell(item));

            add(buy);
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
