package logic;

import main.*;

public class TradeService {

    private final Game game;

    public TradeService(Game game) {
        this.game = game;
    }

    public boolean buy(Item item, int price) {

        if (game.player.money < price) return false;

        game.player.money -= price;
        game.player.addItem(item);
        return true;
    }

    public boolean sell(Item item, int price) {

        if (!game.player.hasItem(item)) return false;

        game.player.money += price;
        game.player.removeItem(item);
        return true;
    }
}
