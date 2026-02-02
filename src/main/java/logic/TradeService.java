// ===== TradeService.java =====
package logic;

import main.*;

public class TradeService {

    private final Game game;

    public TradeService(Game game) {
        this.game = game;
    }

    public boolean buy(GameCharacter seller, Item item, int price) {
        if (seller == null || !seller.hasItem(item) || game.player.money < price) return false;

        seller.removeItem(item);
        game.player.money -= price;
        game.player.addItem(item);
        return true;
    }

    public boolean sell(GameCharacter buyer, Item item, int price) {
        if (buyer == null || !game.player.hasItem(item)) return false;

        game.player.removeItem(item);
        game.player.money += price;
        buyer.addItem(item);
        return true;
    }
}
