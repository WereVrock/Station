package main;

import java.io.Serializable;
import java.util.Random;

public class VisitItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // item reference by name or id
    public String item;

    // null | coinflip | rare | ultra_rare
    public String rarity;

    public boolean isAvailable(Random rng) {
        if (rarity == null) return true;

        switch (rarity) {
            case "coinflip":
                return rng.nextBoolean();
            case "rare":
                return rng.nextInt(4) == 0;
            case "ultra_rare":
                return rng.nextInt(10) == 0;
            default:
                return true;
        }
    }
}
