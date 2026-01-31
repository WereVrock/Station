package logic;

import main.Game;

public class DayService {

    private final Game game;

    public DayService(Game game) {
        this.game = game;
    }

    public void nextDay() {
        game.day++;
        game.waitingForBurnChoice = true;
    }
}
