package ui;

import logic.GameEngine;
import logic.VisitResult;
import main.Item;

import javax.swing.*;
import java.awt.*;

public class TradePanel extends JPanel {

    private final GameEngine engine;
    private final Runnable onEndDay;
    private final Runnable onNextVisit;

    private VisitResult currentVisit;

    public TradePanel(GameEngine engine, Runnable onEndDay, Runnable onNextVisit) {
        this.engine = engine;
        this.onEndDay = onEndDay;
        this.onNextVisit = onNextVisit;
        setLayout(new GridLayout(0, 2, 5, 5));
    }

    public void clear() {
        removeAll();
        currentVisit = null;
        revalidate();
        repaint();
    }

    public void showTrade(VisitResult visit, boolean hasNextVisit) {
        this.currentVisit = visit;
        removeAll();

        // BUY: items character owns
        for (Item item : visit.itemsForSale) {
            JButton buy = new JButton("Buy " + item.name);
            buy.addActionListener(e -> {
                engine.buy(visit.character, item);
                showTrade(visit, hasNextVisit);
            });
            add(buy);
            add(new JLabel(""));
        }

        // SELL: items player owns
        for (Item item : engine.getGame().player.inventory) {
            JButton sell = new JButton("Sell " + item.name);
            sell.addActionListener(e -> {
                engine.sell(visit.character, item);
                showTrade(visit, hasNextVisit);
            });
            add(new JLabel(""));
            add(sell);
        }

        // NEXT VISIT button
        if (hasNextVisit) {
            JButton nextVisitButton = new JButton("Next Visit");
            nextVisitButton.addActionListener(e -> onNextVisit.run());
            add(nextVisitButton);
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
