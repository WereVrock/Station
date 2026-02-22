package logic;

import main.VisitResult;
import logic.visit.VisitService;
import main.Game;
import main.*;
import java.util.*;

public class GameEngine {

    private final Game game;

    private final VisitService visitService;
    private final TradeService tradeService;
    private final ResourceTradeService resourceTradeService;
    private final BurnService burnService;

    public GameEngine(Game game) {
        this.game = game;
        this.visitService = new VisitService(game);
        this.tradeService = new TradeService(game);
        this.resourceTradeService = new ResourceTradeService(game);
        this.burnService = new BurnService();
    }

    public List<VisitResult> burnFuelVisits() {
        FireStatus status = burnService.burnFuel();
        return visitService.resolveAfterBurn(status.getEffect());
    }

    public List<VisitResult> burnItemVisits(Item item) {
        FireStatus status = burnService.burnItem(item);
        return visitService.resolveAfterBurn(status.getEffect());
    }

    public boolean hasPendingVisits() {
        return visitService.hasPendingVisits();
    }

    public void nextDay() {
        visitService.nextDay();
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