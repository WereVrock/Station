package logic;

import main.Game;
import main.GameConstants;

import main.GameCharacter;

public class ResourceTradeService {

    private final Game game;

    public ResourceTradeService(Game game) {
        this.game = game;
    }

    // ===== BUY FROM VISITOR =====

    public boolean buyFoodFromVisit(VisitResult visit, int amount) {
        if (amount <= 0) return false;
        if (visit.sellFood < amount) return false;

        int cost = amount * GameConstants.FOOD_PRICE;
        if (game.player.money < cost) return false;

        game.player.money -= cost;
        game.player.food += amount;
        visit.sellFood -= amount;
        return true;
    }

    public boolean buyFuelFromVisit(VisitResult visit, int amount) {
        if (amount <= 0) return false;
        if (visit.sellFuel < amount) return false;

        int cost = amount * GameConstants.FUEL_PRICE;
        if (game.player.money < cost) return false;

        game.player.money -= cost;
        game.player.fuel += amount;
        visit.sellFuel -= amount;
        return true;
    }

    // ===== SELL TO VISITOR =====

    public boolean sellFoodToVisit(VisitResult visit, int amount) {
        if (amount <= 0) return false;
        if (visit.buyFood < amount) return false;
        if (game.player.food < amount) return false;

        int gain = amount * GameConstants.FOOD_PRICE;

        game.player.food -= amount;
        game.player.money += gain;
        visit.buyFood -= amount;

        return true;
    }

    public boolean sellFuelToVisit(VisitResult visit, int amount) {
        if (amount <= 0) return false;
        if (visit.buyFuel < amount) return false;
        if (game.player.fuel < amount) return false;

        int gain = amount * GameConstants.FUEL_PRICE;

        game.player.fuel -= amount;
        game.player.money += gain;
        visit.buyFuel -= amount;

        return true;
    }

    // ===== CHARACTER RESOURCE SINK (OPTIONAL FUTURE USE) =====

    public void transferBoughtResourcesToCharacter(GameCharacter character,
                                                    int food,
                                                    int fuel) {
        if (character == null) return;
        // intentionally empty for now
        // kept as a hook for later NPC resource simulation
    }
}