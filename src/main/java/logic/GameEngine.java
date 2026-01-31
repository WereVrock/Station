package logic;

import main.*;

import java.util.Optional;
import main.Character;

public class GameEngine {

    private final Game game;
    private final BurnResolver burnResolver;
    private final TradeService tradeService;
    private final DayService dayService;

    public GameEngine(Game game) {
        this.game = game;
        this.burnResolver = new BurnResolver(game);
        this.tradeService = new TradeService(game);
        this.dayService = new DayService(game);
    }

    public Optional<Character> burnFuel() {
        return burnResolver.burnFuel();
    }

    public Optional<Character> burnItem(Item item) {
        return burnResolver.burnItem(item);
    }

    public boolean buy(Item item) {
        return tradeService.buy(item, 5);
    }

    public boolean sell(Item item) {
        return tradeService.sell(item, 3);
    }

    public void nextDay() {
        dayService.nextDay();
    }

    public Game getGame() {
        return game;
    }
}
