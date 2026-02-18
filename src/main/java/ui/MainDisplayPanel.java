package ui;

import main.VisitResult;
import main.Game;
import main.GameCharacter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainDisplayPanel extends JPanel {

    private final JLabel nameLabel;
    private final JLabel imageLabel;
    private final JTextArea dialogueArea;
    private final JScrollPane dialogueScroll;

    private ImageIcon rawFireIcon;
    private ImageIcon rawPortraitIcon;

    public MainDisplayPanel() {
        setLayout(new BorderLayout(10, 10));

        nameLabel = new JLabel("", JLabel.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(nameLabel, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        add(content, BorderLayout.CENTER);

        imageLabel = new JLabel("", JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        dialogueArea = new JTextArea();
        dialogueArea.setEditable(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);

        dialogueScroll = new JScrollPane(dialogueArea);
        dialogueScroll.setBorder(BorderFactory.createEmptyBorder());
        dialogueScroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        dialogueScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        content.add(imageLabel, BorderLayout.CENTER);
        content.add(dialogueScroll, BorderLayout.EAST);

        rawFireIcon = new ImageIcon(getClass().getResource("/images/strong_fire.png"));

        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                rescaleCurrentImage();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustDialogueWidth();
            }
        });
    }

    private void adjustDialogueWidth() {
        int totalWidth = getWidth();
        if (totalWidth <= 0) return;

        int dialogueWidth = Math.max(250, totalWidth / 3);
        dialogueScroll.setPreferredSize(new Dimension(dialogueWidth, getHeight()));
        revalidate();
    }

    private void rescaleCurrentImage() {
        if (imageLabel.getWidth() <= 0 || imageLabel.getHeight() <= 0) return;

        ImageIcon src = rawPortraitIcon != null ? rawPortraitIcon : rawFireIcon;
        if (src == null) return;

        ImageIcon scaled = ImageScaler.scaleToFit(
                src,
                imageLabel.getWidth(),
                imageLabel.getHeight()
        );

        imageLabel.setIcon(scaled);
    }

    public void showBurnPhase(Game game) {
        nameLabel.setText("Day " + game.day);

        rawPortraitIcon = null;

        imageLabel.setText("");
        rescaleCurrentImage();

        dialogueArea.setText("What will you burn today?");
    }

    public void showVisit(VisitResult visit) {
        GameCharacter c = visit.character;

        nameLabel.setText(c.name);

        rawPortraitIcon = c.getPortraitIcon();

        imageLabel.setText("");
        rescaleCurrentImage();

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
        rawPortraitIcon = null;
    }
}