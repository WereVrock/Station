package ui;

import logic.GameEngine;
import logic.VisitResult;
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

        // ---- BUY ----
        for (Item item : new ArrayList<>(visit.itemsForSale)) {
            JButton buy = new JButton(
                "Buy " + item.name + " (" + engine.getBuyPrice(item) + ")"
            );
            buy.setAlignmentX(Component.CENTER_ALIGNMENT);
            buy.addActionListener(e -> {
                if (engine.buyFromVisit(visit, item)) {
                    if (logPanel != null) {
                        logPanel.log("Bought " + item.name + ".");
                    }
                    showTrade(visit, hasNextVisit);
                    refreshStatus();
                }
            });
            add(buy);
            add(Box.createVerticalStrut(5));
        }

        rebuildSellButtons();

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

    // ===== RESTORED METHOD (REAL LOGIC) =====
    private void rebuildSellButtons() {
        List<ItemStack> inventory =
                new ArrayList<>(engine.getGame().player.inventory);

        for (ItemStack stack : inventory) {
            if (!currentVisit.wants(stack.item)) continue;

            JButton sell = new JButton(
                "Sell " + stack.item.name +
                " x" + stack.count +
                " (" + engine.getSellPrice(stack.item) + ")"
            );
            sell.setAlignmentX(Component.CENTER_ALIGNMENT);
            sell.addActionListener(e -> {
                if (engine.sellToVisitCharacter(currentVisit, stack.item)) {
                    if (logPanel != null) {
                        logPanel.log("Sold " + stack.item.name + ".");
                    }
                    showTrade(currentVisit, hasNextVisit);
                    refreshStatus();
                }
            });

            add(Box.createVerticalStrut(5));
            add(sell);
        }
    }

    // ===== RESTORED METHOD =====
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