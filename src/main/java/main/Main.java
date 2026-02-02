package main;
import ui.MainWindow;



import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        Game game = new Game();

        try {
            game.loadItems("items.json");
            game.loadCharacters("characters.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game finalGame = game;

        SwingUtilities.invokeLater(() -> {
            MainWindow ui = new MainWindow(finalGame);
            ui.setVisible(true);
        });
    }
}
