package logic;

import main.GameConstants;

import java.io.Serializable;
import java.util.Random;

public class VisitTradePricing implements Serializable {

    private static final long serialVersionUID = 1L;

    // legacy fixed pricing
    public Integer buyFoodPrice;
    public Integer sellFoodPrice;
    public Integer buyFuelPrice;
    public Integer sellFuelPrice;

    // ranged pricing (content driven)
    public Integer buyFoodMin;
    public Integer buyFoodMax;

    public Integer sellFoodMin;
    public Integer sellFoodMax;

    public Integer buyFuelMin;
    public Integer buyFuelMax;

    public Integer sellFuelMin;
    public Integer sellFuelMax;

    private final transient Random rng = new Random();

    // ===== RESOLUTION =====

    public int resolveBuyFood() {
        if (buyFoodMin != null && buyFoodMax != null)
            return roll(buyFoodMin, buyFoodMax);

        return buyFoodPrice != null ? buyFoodPrice : GameConstants.FOOD_PRICE;
    }

    public int resolveSellFood() {
        if (sellFoodMin != null && sellFoodMax != null)
            return roll(sellFoodMin, sellFoodMax);

        return sellFoodPrice != null ? sellFoodPrice : GameConstants.FOOD_PRICE;
    }

    public int resolveBuyFuel() {
        if (buyFuelMin != null && buyFuelMax != null)
            return roll(buyFuelMin, buyFuelMax);

        return buyFuelPrice != null ? buyFuelPrice : GameConstants.FUEL_PRICE;
    }

    public int resolveSellFuel() {
        if (sellFuelMin != null && sellFuelMax != null)
            return roll(sellFuelMin, sellFuelMax);

        return sellFuelPrice != null ? sellFuelPrice : GameConstants.FUEL_PRICE;
    }

    private int roll(int min, int max) {
        if (max < min) return min;
        return rng.nextInt(max - min + 1) + min;
    }

    @Override
    public String toString() {
        return "VisitTradePricing{" +
                "buyFoodPrice=" + buyFoodPrice +
                ", sellFoodPrice=" + sellFoodPrice +
                ", buyFuelPrice=" + buyFuelPrice +
                ", sellFuelPrice=" + sellFuelPrice +
                ", buyFoodMin=" + buyFoodMin +
                ", buyFoodMax=" + buyFoodMax +
                ", sellFoodMin=" + sellFoodMin +
                ", sellFoodMax=" + sellFoodMax +
                ", buyFuelMin=" + buyFuelMin +
                ", buyFuelMax=" + buyFuelMax +
                ", sellFuelMin=" + sellFuelMin +
                ", sellFuelMax=" + sellFuelMax +
                '}';
    }
}
