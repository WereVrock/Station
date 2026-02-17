package ui;

import logic.visit.VisitResult;
import main.Game;
import main.GameCharacter;

import javax.swing.*;
import java.awt.*;

public class MainDisplayPanel extends JPanel {

    private final JLabel nameLabel;
    private final JLabel imageLabel;
    private final JTextArea dialogueArea;

    private final ImageIcon fireIcon;

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

        fireIcon = loadAndScaleFireIcon();

        add(nameLabel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(new JScrollPane(dialogueArea), BorderLayout.SOUTH);
    }

    private ImageIcon loadAndScaleFireIcon() {
        java.net.URL url = getClass().getResource("/images/strong_fire.png");
        if (url == null) {
            return null;
        }

        ImageIcon raw = new ImageIcon(url);
        Image img = raw.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void showBurnPhase(Game game) {
        nameLabel.setText("Day " + game.day);

        if (fireIcon != null) {
            imageLabel.setText("");
            imageLabel.setIcon(fireIcon);
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("🔥 FIRE 🔥");
        }

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

        dialogueArea.setText("");

        for (String line : visit.dialogue) {
            appendDialogue(line);
        }
    }

    public void appendDialogue(String line) {
        if (!dialogueArea.getText().isEmpty()) {
            dialogueArea.append("\n");
        }
        dialogueArea.append(line);
    }

    public void clear() {
        nameLabel.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("");
        dialogueArea.setText("");
    }
}