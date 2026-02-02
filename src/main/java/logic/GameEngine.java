// ===== GameEngine.java =====
package logic;

import main.*;
import java.util.Optional;
import main.GameCharacter;

public class GameEngine {

    private final Game game;
    private final BurnResolver burnResolver;
    private final TradeService tradeService;
    private final DayService dayService;

    // NEW: active character context
    private GameCharacter activeCharacter;

    public GameEngine(Game game) {
        this.game = game;
        this.burnResolver = new BurnResolver(game);
        this.tradeService = new TradeService(game);
        this.dayService = new DayService(game);
    }

    public Optional<GameCharacter> burnFuel() {
        Optional<GameCharacter> result = burnResolver.burnFuel();
        result.ifPresent(c -> activeCharacter = c);
        return result;
    }

    public Optional<GameCharacter> burnItem(Item item) {
        Optional<GameCharacter> result = burnResolver.burnItem(item);
        result.ifPresent(c -> activeCharacter = c);
        return result;
    }

    // NEW API
    public boolean buy(GameCharacter seller, Item item) {
        return tradeService.buy(seller, item, 5);
    }

    public boolean sell(GameCharacter buyer, Item item) {
        return tradeService.sell(buyer, item, 3);
    }

    // OLD API (restored)
    public boolean buy(Item item) {
        return tradeService.buy(activeCharacter, item, 5);
    }

    public boolean sell(Item item) {
        return tradeService.sell(activeCharacter, item, 3);
    }

    public void nextDay() {
        activeCharacter = null;
        dayService.nextDay();
    }

    public Game getGame() {
        return game;
    }
}
