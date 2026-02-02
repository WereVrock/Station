// ===== GameEngine.java =====
package logic;

import main.*;
import java.util.*;

public class GameEngine {

    private final Game game;
    private final BurnResolver burnResolver;
    private final TradeService tradeService;
    private final DayService dayService;

    private final Queue<GameCharacter> pendingCharacters = new LinkedList<>();
    private boolean burnedToday = false;

    public GameEngine(Game game) {
        this.game = game;
        this.burnResolver = new BurnResolver(game);
        this.tradeService = new TradeService(game);
        this.dayService = new DayService(game);
    }

    public Optional<GameCharacter> burnFuel() {
        if (burnedToday || game.player.fuel <= 0) return Optional.empty();
        burnedToday = true;
        game.player.fuel--;
        game.burnChosen();

        List<GameCharacter> characters = burnResolver.resolveFireMultiple("strongClean");
        pendingCharacters.addAll(characters);
        return getNextCharacter();
    }

    public Optional<GameCharacter> burnItem(Item item) {
        if (burnedToday || !game.player.hasItem(item)) return Optional.empty();
        burnedToday = true;
        game.player.removeItem(item);
        game.worldTags.addAll(item.tags);
        game.burnChosen();

        String fire = item.fireEffect == null || item.fireEffect.isBlank() ? "weakClean" : item.fireEffect;
        List<GameCharacter> characters = burnResolver.resolveFireMultiple(fire);
        pendingCharacters.addAll(characters);
        return getNextCharacter();
    }

    private Optional<GameCharacter> getNextCharacter() {
        return Optional.ofNullable(pendingCharacters.poll());
    }

    public Optional<GameCharacter> nextCharacter() {
        return getNextCharacter();
    }

    public boolean buy(GameCharacter seller, Item item) {
        return tradeService.buy(seller, item, 5);
    }

    public boolean sell(GameCharacter buyer, Item item) {
        return tradeService.sell(buyer, item, 3);
    }

    public void nextDay() {
        pendingCharacters.clear();
        burnedToday = false;
        dayService.nextDay();
    }

    public Game getGame() {
        return game;
    }

    public boolean hasPendingCharacters() {
        return !pendingCharacters.isEmpty();
    }
}
