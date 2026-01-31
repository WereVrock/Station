package ui;

import logic.GameEngine;
import main.*;
import main.Character;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class MainWindow extends JFrame {

    private final GameEngine engine;

    private JLabel portraitLabel;
    private JTextArea logArea;
    private JPanel burnPanel;
    private JPanel tradePanel;
    private JLabel resourceLabel;
    private JLabel dayLabel;

    private Character currentCharacter;

    public MainWindow(Game game) {
        this.engine = new GameEngine(game);

        setTitle("Cozy Apocalypse");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        dayLabel = new JLabel("", JLabel.CENTER);
        add(dayLabel, BorderLayout.NORTH);

        portraitLabel = new JLabel();
        portraitLabel.setHorizontalAlignment(JLabel.CENTER);
        add(portraitLabel, BorderLayout.EAST);

        resourceLabel = new JLabel();
        resourceLabel.setHorizontalAlignment(JLabel.CENTER);
        add(resourceLabel, BorderLayout.WEST);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        burnPanel = new JPanel(new GridLayout(0, 4, 5, 5));
        tradePanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JButton invBtn = new JButton("Show Inventory");
        invBtn.addActionListener(e -> showInventory());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(burnPanel, BorderLayout.NORTH);
        bottom.add(tradePanel, BorderLayout.CENTER);
        bottom.add(invBtn, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        refreshAll();
        startDayMessage();
    }

    private Game game() {
        return engine.getGame();
    }

    // ------------------------------------------------
    // INVENTORY
    // ------------------------------------------------

    private void showInventory() {
        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Food: ").append(game().player.food).append("\n");
        sb.append("Fuel: ").append(game().player.fuel).append("\n\n");

        for (Item item : game().player.inventory) {
            sb.append(item.name)
              .append(" | ")
              .append(item.type)
              .append("\n");
        }

        area.setText(sb.toString());
        JOptionPane.showMessageDialog(this, new JScrollPane(area));
    }

    // ------------------------------------------------
    // DAY
    // ------------------------------------------------

    private void startDayMessage() {
        dayLabel.setText("Day " + game().day);
        logArea.append("\nDay " + game().day + "\n");
        logArea.append("What will you burn today?\n");
    }

    // ------------------------------------------------
    // REFRESH
    // ------------------------------------------------

    private void refreshAll() {
        refreshResources();
        refreshBurnButtons();
    }

    private void refreshResources() {
        resourceLabel.setText(
            "<html>" +
                "Food: " + game().player.food + "<br>" +
                "Fuel: " + game().player.fuel + "<br>" +
                "Money: " + game().player.money +
            "</html>"
        );
    }

    // ------------------------------------------------
    // BURN UI
    // ------------------------------------------------

    private void refreshBurnButtons() {
        burnPanel.removeAll();

        if (!game().waitingForBurnChoice) {
            return;
        }

        if (game().player.fuel > 0) {
            JButton fuelBtn = new JButton("Burn Fuel");
            fuelBtn.addActionListener(e -> {
                logArea.append("You burn fuel.\n");
                handleBurn(engine.burnFuel());
            });
            burnPanel.add(fuelBtn);
        }

        for (Item item : game().player.inventory) {
            if (!item.burnable) continue;

            JButton btn = new JButton(item.name);
            btn.addActionListener(e -> {
                logArea.append("You burn: " + item.name + "\n");
                handleBurn(engine.burnItem(item));
            });
            burnPanel.add(btn);
        }

        burnPanel.revalidate();
        burnPanel.repaint();
    }

    private void handleBurn(Optional<Character> result) {
        if (result.isPresent()) {
            currentCharacter = result.get();
            portraitLabel.setIcon(new ImageIcon(currentCharacter.name + ".png"));
            logArea.append(currentCharacter.name + " appears!\n");
            logArea.append(currentCharacter.background + "\n");
            setupTrade();
        } else {
            logArea.append("No one appears.\n");
        }

        refreshAll();
    }

    // ------------------------------------------------
    // TRADE UI
    // ------------------------------------------------

    private void setupTrade() {
        tradePanel.removeAll();

        for (Item item : game().items) {
            JButton buy = new JButton("Buy " + item.name);
            buy.addActionListener(e -> {
                if (engine.buy(item)) {
                    logArea.append(currentCharacter.name + " sells " + item.name + "\n");
                } else {
                    logArea.append("Not enough money\n");
                }
                refreshAll();
            });

            JButton sell = new JButton("Sell " + item.name);
            sell.addActionListener(e -> {
                if (engine.sell(item)) {
                    logArea.append(currentCharacter.name + " buys " + item.name + "\n");
                }
                refreshAll();
            });

            tradePanel.add(buy);
            tradePanel.add(sell);
        }

        JButton nextDay = new JButton("End Day");
        nextDay.addActionListener(e -> endDay());
        tradePanel.add(nextDay);

        tradePanel.revalidate();
        tradePanel.repaint();
    }

    private void endDay() {
        engine.nextDay();
        currentCharacter = null;
        portraitLabel.setIcon(null);
        tradePanel.removeAll();
        startDayMessage();
        refreshAll();
    }
}
