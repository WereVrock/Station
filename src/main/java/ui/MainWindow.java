// ===== MainWindow.java =====
package ui;

import logic.GameEngine;
import main.*;
import main.GameCharacter;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class MainWindow extends JFrame {

    private final GameEngine engine;

    private final StatusPanel statusPanel;
    private final LogPanel logPanel;
    private final BurnPanel burnPanel;
    private final TradePanel tradePanel;

    private GameCharacter currentCharacter;

    public MainWindow(Game game) {
        this.engine = new GameEngine(game);

        setTitle("Cozy Apocalypse");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusPanel = new StatusPanel(engine);
        logPanel = new LogPanel();
        burnPanel = new BurnPanel(engine, this::handleBurn);
        tradePanel = new TradePanel(engine, this::endDay);

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
    }

    private void handleBurn(Optional<GameCharacter> result) {
        burnPanel.refresh();

        if (result.isPresent()) {
            currentCharacter = result.get();
            logPanel.characterAppears(currentCharacter);
            tradePanel.showTrade(currentCharacter);
        } else {
            currentCharacter = null;
            logPanel.noOneAppears();
            tradePanel.showEndDayOnly();
        }

        statusPanel.refresh();
    }

    private void endDay() {
        engine.nextDay();
        currentCharacter = null;
        startDay();
    }
}
