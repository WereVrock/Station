package ui;

import logic.GameEngine;
import logic.VisitResult;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private final GameEngine engine;

    private final StatusPanel statusPanel;
    private final LogPanel logPanel;
    private final BurnPanel burnPanel;
    private final TradePanel tradePanel;

    private List<VisitResult> currentVisits;
    private int currentVisitIndex = 0;

    public MainWindow(Game game) {
        this.engine = new GameEngine(game);

        setTitle("Cozy Apocalypse");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusPanel = new StatusPanel(engine);
        logPanel = new LogPanel();
        burnPanel = new BurnPanel(engine, this::handleBurnVisits);
        tradePanel = new TradePanel(engine, this::endDay, this::nextVisit);

        // 🔴 THIS WAS MISSING
        tradePanel.setStatusPanel(statusPanel);

        add(statusPanel, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);
        add(burnPanel, BorderLayout.SOUTH);
        add(tradePanel, BorderLayout.EAST);

        startDay();
    }

    private void startDay() {
        statusPanel.refresh();
        burnPanel.refresh();
        tradePanel.clear();
        logPanel.dayStart(engine.getGame().day);

        currentVisits = null;
        currentVisitIndex = 0;
    }

    private void handleBurnVisits(List<VisitResult> visits) {
        burnPanel.refresh();

        if (visits.isEmpty()) {
            logPanel.noOneAppears();
            tradePanel.showEndDayOnly();
            return;
        }

        currentVisits = visits;
        currentVisitIndex = 0;

        showCurrentVisit();
    }

    private void showCurrentVisit() {
        if (currentVisits == null || currentVisitIndex >= currentVisits.size()) {
            tradePanel.showEndDayOnly();
            return;
        }

        VisitResult visit = currentVisits.get(currentVisitIndex);

        System.out.println(visit);

        logPanel.visitAppears(visit);

        boolean hasNext = currentVisitIndex < currentVisits.size() - 1;
        tradePanel.showTrade(visit, hasNext);
    }

    private void nextVisit() {
        currentVisitIndex++;
        showCurrentVisit();
    }

    private void endDay() {
        engine.nextDay();
        currentVisits = null;
        currentVisitIndex = 0;
        startDay();
    }
}
