package ui;

import logic.GameEngine;
import logic.VisitResult;
import main.GameConstants;
import main.Item;
import main.ItemStack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TradePanel extends JPanel {

    private final GameEngine engine;
    private final Runnable onEndDay;
    private final Runnable onNextVisit;

    private StatusPanel statusPanel;
    private LogPanel logPanel;
    private MainDisplayPanel displayPanel;

    private VisitResult currentVisit;
    private boolean hasNextVisit;

    public TradePanel(GameEngine engine, Runnable onEndDay, Runnable onNextVisit) {
        this.engine = engine;
        this.onEndDay = onEndDay;
        this.onNextVisit = onNextVisit;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public void setLogPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
    }

    public void setDisplayPanel(MainDisplayPanel displayPanel) {
        this.displayPanel = displayPanel;
    }

    public void clear() {
        removeAll();
        currentVisit = null;
        refresh();
    }

    public void showTrade(VisitResult visit, boolean hasNextVisit) {
        this.currentVisit = visit;
        this.hasNextVisit = hasNextVisit;

        removeAll();
        add(Box.createVerticalGlue());

        for (Item item : new ArrayList<>(visit.itemsForSale)) {
            JButton buy = new JButton(
                "Buy " + item.name + " (" + engine.getBuyPrice(item) + ")"
            );
            buy.setAlignmentX(Component.CENTER_ALIGNMENT);
            buy.addActionListener(e -> {
                if (engine.buyFromVisit(visit, item)) {
                    logPanel.log("Bought " + item.name + ".");
                    showTrade(visit, hasNextVisit);
                    refreshStatus();
                }
            });
            add(buy);
            add(Box.createVerticalStrut(5));
        }

        addResourceBuyButtons();
        rebuildSellButtons();
        addResourceSellButtons();

        if (hasNextVisit) {
            JButton next = new JButton("Next Visit");
            next.setAlignmentX(Component.CENTER_ALIGNMENT);
            next.addActionListener(e -> onNextVisit.run());
            add(Box.createVerticalStrut(10));
            add(next);
        }

        add(Box.createVerticalStrut(10));
        addEndDayButton();
        add(Box.createVerticalGlue());

        refresh();
    }

    private void addResourceBuyButtons() {
        if (currentVisit.sellFood > 0) {
            JButton buyFood = new JButton(
                "Buy Food x1 (" + GameConstants.FOOD_PRICE + ")"
            );
            buyFood.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyFood.addActionListener(e -> {
                boolean wasLast = currentVisit.sellFood == 1;

                if (engine.buyFoodFromVisit(currentVisit, 1)) {
                    logPanel.log("Bought food.");

                    if (wasLast) {
                        displayPanel.appendDialogue(exhaustedSellText());
                    }

                    showTrade(currentVisit, hasNextVisit);
                    refreshStatus();
                }
            });
            add(Box.createVerticalStrut(5));
            add(buyFood);
        }

        if (currentVisit.sellFuel > 0) {
            JButton buyFuel = new JButton(
                "Buy Fuel x1 (" + GameConstants.FUEL_PRICE + ")"
            );
            buyFuel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyFuel.addActionListener(e -> {
                boolean wasLast = currentVisit.sellFuel == 1;

                if (engine.buyFuelFromVisit(currentVisit, 1)) {
                    logPanel.log("Bought fuel.");

                    if (wasLast) {
                        displayPanel.appendDialogue(exhaustedSellText());
                    }

                    showTrade(currentVisit, hasNextVisit);
                    refreshStatus();
                }
            });
            add(Box.createVerticalStrut(5));
            add(buyFuel);
        }
    }

    private void rebuildSellButtons() {
        List<ItemStack> inventory =
                new ArrayList<>(engine.getGame().player.inventory);

        for (ItemStack stack : inventory) {
            if (!currentVisit.wants(stack.item)) continue;

            String label = stack.count > 1
                    ? stack.item.name + " x" + stack.count
                    : stack.item.name;

            JButton sell = new JButton(
                "Sell " + label + " (" + engine.getSellPrice(stack.item) + ")"
            );
            sell.setAlignmentX(Component.CENTER_ALIGNMENT);
            sell.addActionListener(e -> {
                if (engine.sellToVisitCharacter(currentVisit, stack.item)) {
                    logPanel.log("Sold " + stack.item.name + ".");
                    showTrade(currentVisit, hasNextVisit);
                    refreshStatus();
                }
            });

            add(Box.createVerticalStrut(5));
            add(sell);
        }
    }

    private void addResourceSellButtons() {
        if (currentVisit.buyFood > 0) {
            JButton sellFood = new JButton(
                "Sell Food x1 (" + GameConstants.FOOD_PRICE + ")"
            );
            sellFood.setAlignmentX(Component.CENTER_ALIGNMENT);
            sellFood.addActionListener(e -> {
                boolean wasLast = currentVisit.buyFood == 1;

                if (engine.sellFoodToVisit(currentVisit, 1)) {
                    logPanel.log("Sold food.");

                    if (wasLast) {
                        displayPanel.appendDialogue(exhaustedBuyText());
                    }

                    showTrade(currentVisit, hasNextVisit);
                    refreshStatus();
                }
            });
            add(Box.createVerticalStrut(5));
            add(sellFood);
        }

        if (currentVisit.buyFuel > 0) {
            JButton sellFuel = new JButton(
                "Sell Fuel x1 (" + GameConstants.FUEL_PRICE + ")"
            );
            sellFuel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sellFuel.addActionListener(e -> {
                boolean wasLast = currentVisit.buyFuel == 1;

                if (engine.sellFuelToVisit(currentVisit, 1)) {
                    logPanel.log("Sold fuel.");

                    if (wasLast) {
                        displayPanel.appendDialogue(exhaustedBuyText());
                    }

                    showTrade(currentVisit, hasNextVisit);
                    refreshStatus();
                }
            });
            add(Box.createVerticalStrut(5));
            add(sellFuel);
        }
    }

    private String exhaustedSellText() {
        return "They tuck the last of it away. \"That’s everything I had.\"";
    }

    private String exhaustedBuyText() {
        return "They nod, satisfied. \"That’ll do.\"";
    }

    public void showEndDayOnly() {
        removeAll();
        add(Box.createVerticalGlue());
        addEndDayButton();
        add(Box.createVerticalGlue());
        refresh();
    }

    public void addEndDayButton() {
        JButton endDay = new JButton("End Day");
        endDay.setAlignmentX(Component.CENTER_ALIGNMENT);
        endDay.addActionListener(e -> onEndDay.run());
        add(endDay);
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    private void refreshStatus() {
        if (statusPanel != null) {
            statusPanel.refresh();
        }
    }
}