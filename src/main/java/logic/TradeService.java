package logic;

import main.Game;
import main.Item;

public class TradeService {

    private final Game game;

    public TradeService(Game game) {
        this.game = game;
    }

    // Price player pays to buy from character (LOW)
    public int getBuyPrice(Item item) {
        int base = item.basePrice > 0 ? item.basePrice : 5;
        return Math.max(1, base - 2);
    }

    // Price player gets for selling to character (HIGH)
    public int getSellPrice(Item item) {
        int base = item.basePrice > 0 ? item.basePrice : 5;
        return base + 2;
    }
}
