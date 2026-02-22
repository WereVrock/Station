package main;

import logic.GameContentValidator;
import ui.MainWindow;

import javax.swing.SwingUtilities;

public class Main {

    public static Game game;
    public static void main(String[] args) {

        game = new Game();

        try {
            game.loadItems("items.json");

            // Use new content system (triggers compiled here)
            game.loadContent("characters.json");

            GameContentValidator validator = new GameContentValidator(game);
            validator.setFailOnError(true);
            validator.validate();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Game finalGame = game;

        SwingUtilities.invokeLater(() -> {
            MainWindow ui = new MainWindow(finalGame);
            ui.setVisible(true);
        });
    }
}