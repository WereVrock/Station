package ui;

import logic.GameEngine;
import logic.VisitResult;
import main.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TradePanel extends JPanel {

    private final GameEngine engine;
    private final Runnable onEndDay;
    private final Runnable onNextVisit;
    private StatusPanel statusPanel;

    private VisitResult currentVisit;
    private boolean hasNextVisit;

    public TradePanel(GameEngine engine, Runnable onEndDay, Runnable onNextVisit) {
        this.engine = engine;
        this.onEndDay = onEndDay;
        this.onNextVisit = onNextVisit;
        setLayout(new GridLayout(0, 2, 5, 5));
    }

    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public void clear() {
        removeAll();
        currentVisit = null;
        revalidate();
        repaint();
    }

    public void showTrade(VisitResult visit, boolean hasNextVisit) {
        this.currentVisit = visit;
        this.hasNextVisit = hasNextVisit;

        removeAll();

        // BUY buttons
        for (Item item : new ArrayList<>(visit.itemsForSale)) {
            JButton buy = new JButton("Buy " + item.name);
            buy.addActionListener(e -> {
                if (engine.buyFromVisit(visit, item)) {
                    System.out.println("Bought " + item.name + ". Gold now: " + engine.getGame().player.money);
                    showTrade(visit, hasNextVisit); // rebuild panel
                    refreshStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot buy item (not enough money).");
                }
            });
            add(buy);
            add(new JLabel("Price: " + engine.getBuyPrice(item)));
        }

        // SELL buttons
        rebuildSellButtons();

        // NEXT VISIT button
        if (hasNextVisit) {
            JButton nextVisitButton = new JButton("Next Visit");
            nextVisitButton.addActionListener(e -> onNextVisit.run());
            add(nextVisitButton);
        }

        addEndDayButton();
        refresh();
    }

    private void rebuildSellButtons() {
        List<Item> playerItems = new ArrayList<>(engine.getGame().player.inventory);
        for (Item item : playerItems) {
            JButton sell = new JButton("Sell " + item.name);
            sell.addActionListener(e -> {
                if (engine.sellToVisitCharacter(currentVisit, item)) {
                    System.out.println("Sold " + item.name + ". Gold now: " + engine.getGame().player.money);
                    showTrade(currentVisit, hasNextVisit); // rebuild panel
                    refreshStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot sell item.");
                }
            });
            add(new JLabel(""));
            add(new JLabel("Price: " + engine.getSellPrice(item)));
        }
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

    private void refreshStatus() {
        if (statusPanel != null) statusPanel.refresh();
    }
}
