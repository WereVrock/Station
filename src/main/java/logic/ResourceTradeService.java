package logic;

import main.Game;
import main.GameCharacter;

public class ResourceTradeService {

    private final Game game;

    public ResourceTradeService(Game game) {
        this.game = game;
    }

    public boolean buyFoodFromVisit(VisitResult visit, int amount) {
        if (amount <= 0 || visit.sellFood < amount) return false;
        int cost = amount * visit.sellFoodPrice;
        if (game.player.money < cost) return false;

        game.player.money -= cost;
        game.player.food += amount;
        visit.sellFood -= amount;
        return true;
    }

    public boolean buyFuelFromVisit(VisitResult visit, int amount) {
        if (amount <= 0 || visit.sellFuel < amount) return false;
        int cost = amount * visit.sellFuelPrice;
        if (game.player.money < cost) return false;

        game.player.money -= cost;
        game.player.fuel += amount;
        visit.sellFuel -= amount;
        return true;
    }

    public boolean sellFoodToVisit(VisitResult visit, int amount) {
        if (amount <= 0 || visit.buyFood < amount || game.player.food < amount) return false;
        int gain = amount * visit.buyFoodPrice;

        game.player.food -= amount;
        game.player.money += gain;
        visit.buyFood -= amount;
        return true;
    }

    public boolean sellFuelToVisit(VisitResult visit, int amount) {
        if (amount <= 0 || visit.buyFuel < amount || game.player.fuel < amount) return false;
        int gain = amount * visit.buyFuelPrice;

        game.player.fuel -= amount;
        game.player.money += gain;
        visit.buyFuel -= amount;
        return true;
    }

    public void transferBoughtResourcesToCharacter(GameCharacter character,
                                                    int food,
                                                    int fuel) {
        if (character == null) return;
    }
}
