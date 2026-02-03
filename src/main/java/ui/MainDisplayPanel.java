package ui;

import logic.VisitResult;
import main.Game;
import main.GameCharacter;

import javax.swing.*;
import java.awt.*;

public class MainDisplayPanel extends JPanel {

    private final JLabel nameLabel;
    private final JLabel imageLabel;
    private final JTextArea dialogueArea;

    public MainDisplayPanel() {
        setLayout(new BorderLayout(10, 10));

        nameLabel = new JLabel("", JLabel.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));

        imageLabel = new JLabel("", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        dialogueArea = new JTextArea();
        dialogueArea.setEditable(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);

        add(nameLabel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(new JScrollPane(dialogueArea), BorderLayout.SOUTH);
    }

    public void showBurnPhase(Game game) {
        nameLabel.setText("Day " + game.day);
        imageLabel.setIcon(null);
        imageLabel.setText("🔥 FIRE 🔥");
        dialogueArea.setText("What will you burn today?");
    }

    public void showVisit(VisitResult visit) {
        GameCharacter c = visit.character;

        nameLabel.setText(c.name);

        ImageIcon icon = c.getPortraitIcon();
        if (icon != null) {
            imageLabel.setText("");
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("Character Image");
        }

        StringBuilder sb = new StringBuilder();
        for (String line : visit.dialogue) {
            sb.append(line).append("\n");
        }

        dialogueArea.setText(sb.toString());
    }

    public void clear() {
        nameLabel.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("");
        dialogueArea.setText("");
    }
}
