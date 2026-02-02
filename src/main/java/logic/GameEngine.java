package logic;

import main.*;
import java.util.*;

public class GameEngine {

    private final Game game;
    private final BurnResolver burnResolver;
    private final TradeService tradeService;
    private final DayService dayService;

    private final Queue<VisitResult> pendingVisits = new LinkedList<>();
    private boolean burnedToday = false;

    public GameEngine(Game game) {
        this.game = game;
        this.burnResolver = new BurnResolver(game);
        this.tradeService = new TradeService(game);
        this.dayService = new DayService(game);
    }

    public List<VisitResult> burnFuelVisits() {
        if (burnedToday || game.player.fuel <= 0) return Collections.emptyList();
        burnedToday = true;
        game.player.fuel--;
        game.burnChosen();

        List<VisitResult> visits = burnResolver.resolveFireMultiple("strongClean");
        pendingVisits.addAll(visits);
        return getNextVisits();
    }

    public List<VisitResult> burnItemVisits(Item item) {
        if (burnedToday || !game.player.hasItem(item)) return Collections.emptyList();
        burnedToday = true;
        game.player.removeItem(item);
        game.worldTags.addAll(item.tags);
        game.burnChosen();

        String fire = item.fireEffect == null || item.fireEffect.isBlank() ? "weakClean" : item.fireEffect;
        List<VisitResult> visits = burnResolver.resolveFireMultiple(fire);
        pendingVisits.addAll(visits);
        return getNextVisits();
    }

    private List<VisitResult> getNextVisits() {
        List<VisitResult> result = new ArrayList<>();
        while (!pendingVisits.isEmpty() && result.size() < 5) { // max 5 visits per day
            result.add(pendingVisits.poll());
        }
        return result;
    }

    public boolean hasPendingVisits() {
        return !pendingVisits.isEmpty();
    }

    public boolean buy(GameCharacter seller, Item item) {
        return tradeService.buy(seller, item, 5);
    }

    public boolean sell(GameCharacter buyer, Item item) {
        return tradeService.sell(buyer, item, 3);
    }

    public void nextDay() {
        pendingVisits.clear();
        burnedToday = false;
        dayService.nextDay();
    }

    public Game getGame() {
        return game;
    }
}
