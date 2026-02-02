package logic;

import main.*;
import java.util.Optional;
import main.GameCharacter;

public class GameEngine {

    private final Game game;
    private final BurnResolver burnResolver;
    private final TradeService tradeService;
    private final DayService dayService;

    private GameCharacter activeCharacter;

    public GameEngine(Game game) {
        this.game = game;
        this.burnResolver = new BurnResolver(game);
        this.tradeService = new TradeService(game);
        this.dayService = new DayService(game);
    }

    public Optional<GameCharacter> burnFuel() {
        if (game.visitsToday >= 5) {
            System.out.println("The fire burns, but no one comes. The day is too crowded.");
            return Optional.empty();
        }

        Optional<GameCharacter> result = burnResolver.burnFuel();
        result.ifPresent(c -> {
            activeCharacter = c;
            game.visitsToday++;
        });
        return result;
    }

    public Optional<GameCharacter> burnItem(Item item) {
        if (game.visitsToday >= 5) {
            System.out.println("The fire crackles, but the day allows no more visitors.");
            return Optional.empty();
        }

        Optional<GameCharacter> result = burnResolver.burnItem(item);
        result.ifPresent(c -> {
            activeCharacter = c;
            game.visitsToday++;
        });
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
