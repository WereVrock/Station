package main;

import logic.GameContentValidator;
import ui.MainWindow;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        Game game = new Game();

        try {
            game.loadItems("items.json");
            game.loadCharacters("characters.json");

            GameContentValidator validator = new GameContentValidator(game);
            validator.setFailOnError(true); // change to false for warning-only mode
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
