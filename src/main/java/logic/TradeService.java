// ===== TradeService.java =====
package logic;

import main.*;
import main.GameCharacter;

public class TradeService {

    private final Game game;

    public TradeService(Game game) {
        this.game = game;
    }

    // NEW: character-aware buyer
    public boolean buy(GameCharacter seller, Item item, int price) {
        if (seller == null) return false;
        if (!seller.hasItem(item)) return false;
        if (game.player.money < price) return false;

        seller.removeItem(item);
        game.player.money -= price;
        game.player.addItem(item);
        return true;
    }

    // NEW: character-aware sell
    public boolean sell(GameCharacter buyer, Item item, int price) {
        if (buyer == null) return false;
        if (!game.player.hasItem(item)) return false;

        game.player.removeItem(item);
        game.player.money += price;
        buyer.addItem(item);
        return true;
    }

    // OLD SIGNATURES (restored for compatibility)

    public boolean buy(Item item, int price) {
        return false; // no character context → not allowed
    }

    public boolean sell(Item item, int price) {
        return false; // no character context → not allowed
    }
}
