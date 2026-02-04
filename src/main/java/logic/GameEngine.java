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
        game.player.fuel -= GameConstants.FUEL_BURN_COST;
        game.burnChosen();

        List<VisitResult> visits = burnResolver.resolveFireMultiple("strongClean");
        pendingVisits.addAll(visits);
        resolveRandomVisitsIfNeeded();

        return getNextVisits();
    }

    public List<VisitResult> burnItemVisits(Item item) {
        if (burnedToday || !game.player.hasItem(item)) return Collections.emptyList();

        burnedToday = true;
        game.player.removeItem(item);
        game.worldTags.addAll(item.tags);
        game.burnChosen();

        String fire = (item.fireEffect == null || item.fireEffect.isBlank())
                ? "weakClean"
                : item.fireEffect;

        List<VisitResult> visits = burnResolver.resolveFireMultiple(fire);
        pendingVisits.addAll(visits);
        resolveRandomVisitsIfNeeded();

        return getNextVisits();
    }

    private void resolveRandomVisitsIfNeeded() {
        int visitsToday = game.visitsToday + pendingVisits.size();
        if (visitsToday >= GameConstants.VISITS_RANDOM_TRIGGER_THRESHOLD) return;

        List<VisitResult> randoms = burnResolver.resolveRandomVisits();
        for (VisitResult r : randoms) {
            if (pendingVisits.size() + game.visitsToday >= GameConstants.VISITS_MAX_PER_DAY) break;
            pendingVisits.add(r);
        }
    }

    private List<VisitResult> getNextVisits() {
        List<VisitResult> result = new ArrayList<>();

        while (!pendingVisits.isEmpty() &&
                result.size() + game.visitsToday < GameConstants.VISITS_MAX_PER_DAY) {
            result.add(pendingVisits.poll());
        }

        game.visitsToday += result.size();
        return result;
    }

    public boolean hasPendingVisits() {
        return !pendingVisits.isEmpty();
    }

    public boolean buyFromVisit(VisitResult visit, Item item) {
        int price = tradeService.getBuyPrice(item);

        if (game.player.money >= price && visit.itemsForSale.contains(item)) {
            game.player.money -= price;
            game.player.addItem(item);
            visit.itemsForSale.remove(item);
            return true;
        }
        return false;
    }

    public boolean sellToVisitCharacter(VisitResult visit, Item item) {
        if (visit.character == null) return false;
        if (!game.player.hasItem(item)) return false;
        if (!visit.wants(item)) return false;

        int price = tradeService.getSellPrice(item);
        game.player.money += price;
        game.player.removeItem(item);
        visit.character.addItem(item);
        return true;
    }

    public int getBuyPrice(Item item) {
        return tradeService.getBuyPrice(item);
    }

    public int getSellPrice(Item item) {
        return tradeService.getSellPrice(item);
    }

    public void nextDay() {
        pendingVisits.clear();
        burnedToday = false;
        game.visitsToday = 0;
        dayService.nextDay();
    }

    public Game getGame() {
        return game;
    }
}