// ===== TradeService.java =====
package logic;

import main.Game;
import main.Item;

public class TradeService {

    private final Game game;

    public TradeService(Game game) {
        this.game = game;
    }

    // Price player pays to buy from character
    public int getBuyPrice(Item item) {
        // Can be extended per-item logic, e.g., item.type or rarity
        return 5;
    }

    // Price player gets for selling to character
    public int getSellPrice(Item item) {
        // Can be extended per-item logic
        return 3;
    }
}
