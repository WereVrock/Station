// ===== DayService.java =====
package logic;

import main.Game;
import main.GameCharacter;

public class DayService {

    private final Game game;

    public DayService(Game game) {
        this.game = game;
    }

    public void nextDay() {
        game.day++;
        game.waitingForBurnChoice = true;

        // RESET DAILY VISIT STATE
        for (GameCharacter c : game.characters) {
            c.visitedToday = false;
        }
    }
}
