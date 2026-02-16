package ui;

import logic.GameEngine;
import logic.visit.VisitResult;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private final GameEngine engine;

    private final StatusPanel statusPanel;
    private final MainDisplayPanel displayPanel;
    private final LogPanel logPanel;
    private final BurnPanel burnPanel;
    private final TradePanel tradePanel;

    private List<VisitResult> currentVisits;
    private int currentVisitIndex = 0;

    public MainWindow(Game game) {
        engine = new GameEngine(game);

        setTitle("Cozy Apocalypse");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusPanel = new StatusPanel(engine);
        displayPanel = new MainDisplayPanel();
        logPanel = new LogPanel();

        burnPanel = new BurnPanel(engine, this::handleBurnVisits);
        tradePanel = new TradePanel(engine, this::endDay, this::nextVisit);

        tradePanel.setStatusPanel(statusPanel);
        tradePanel.setLogPanel(logPanel);
        tradePanel.setDisplayPanel(displayPanel);

        JPanel center = new JPanel(new BorderLayout());
        center.add(displayPanel, BorderLayout.CENTER);
        center.add(logPanel, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout());
        right.add(burnPanel, BorderLayout.CENTER);
        right.add(tradePanel, BorderLayout.SOUTH);

        add(statusPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        startDay();
        setVisible(true);
    }

    private void startDay() {
        statusPanel.refresh();
        burnPanel.refresh();
        tradePanel.clear();
        logPanel.clear();
        displayPanel.showBurnPhase(engine.getGame());

        currentVisits = null;
        currentVisitIndex = 0;
    }

    private void handleBurnVisits(List<VisitResult> visits) {
        statusPanel.refresh();
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
        
        logPanel.visitAppears(visit);
        displayPanel.showVisit(visit);

        boolean hasNext = currentVisitIndex < currentVisits.size() - 1;
        tradePanel.showTrade(visit, hasNext);
    }

    private void nextVisit() {
        currentVisitIndex++;
        showCurrentVisit();
    }

    private void endDay() {
        engine.nextDay();
        startDay();
    }
}