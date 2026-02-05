package logic;

import main.*;
import java.util.*;

public class GameEngine {

    private final Game game;

    private final VisitService visitService;
    private final TradeService tradeService;
    private final ResourceTradeService resourceTradeService;

    public GameEngine(Game game) {
        this.game = game;
        this.visitService = new VisitService(game);
        this.tradeService = new TradeService(game);
        this.resourceTradeService = new ResourceTradeService(game);
    }

    // ===== VISITS =====

    public List<VisitResult> burnFuelVisits() {
        return visitService.burnFuel();
    }

    public List<VisitResult> burnItemVisits(Item item) {
        return visitService.burnItem(item);
    }

    public boolean hasPendingVisits() {
        return visitService.hasPendingVisits();
    }

    public void nextDay() {
        visitService.nextDay();
    }

    // ===== ITEM TRADING =====

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

    // ===== FOOD / FUEL =====

    public boolean buyFoodFromVisit(VisitResult visit, int amount) {
        return resourceTradeService.buyFoodFromVisit(visit, amount);
    }

    public boolean buyFuelFromVisit(VisitResult visit, int amount) {
        return resourceTradeService.buyFuelFromVisit(visit, amount);
    }

    public boolean sellFoodToVisit(VisitResult visit, int amount) {
        return resourceTradeService.sellFoodToVisit(visit, amount);
    }

    public boolean sellFuelToVisit(VisitResult visit, int amount) {
        return resourceTradeService.sellFuelToVisit(visit, amount);
    }

    public int getBuyPrice(Item item) {
        return tradeService.getBuyPrice(item);
    }

    public int getSellPrice(Item item) {
        return tradeService.getSellPrice(item);
    }

    public Game getGame() {
        return game;
    }
}