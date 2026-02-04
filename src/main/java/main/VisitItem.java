package main;

import java.io.Serializable;
import java.util.Random;

public class VisitItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public String item;
    public String rarity;

    public boolean isAvailable(Random rng) {
        if (rarity == null) return true;

        switch (rarity) {
            case "coinflip":
                return rng.nextBoolean();
            case "rare":
                return rng.nextInt(GameConstants.VISITITEM_RARE_CHANCE) == 0;
            case "ultra_rare":
                return rng.nextInt(GameConstants.VISITITEM_ULTRA_RARE_CHANCE) == 0;
            default:
                return true;
        }
    }
}