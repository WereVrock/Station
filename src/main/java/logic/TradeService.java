package logic;

import main.Game;
import main.Item;
import main.GameConstants;

public class TradeService {

    private final Game game;

    public TradeService(Game game) {
        this.game = game;
    }

    public int getBuyPrice(Item item) {
        int base = item.basePrice > 0 ? item.basePrice : GameConstants.TRADE_DEFAULT_BASE_PRICE;
        return Math.max(GameConstants.TRADE_MIN_PRICE, base - GameConstants.TRADE_BUY_DELTA);
    }

    public int getSellPrice(Item item) {
        int base = item.basePrice > 0 ? item.basePrice : GameConstants.TRADE_DEFAULT_BASE_PRICE;
        return base + GameConstants.TRADE_SELL_DELTA;
    }
}