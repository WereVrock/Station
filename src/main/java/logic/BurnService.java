package logic;

import main.FireStatus;
import main.Game;
import main.Item;

public class BurnService {

    private final Game game;

    public BurnService(Game game) {
        this.game = game;
    }

    public FireStatus burnFuel() {
        // Fuel ALWAYS produces strong clean fire
        return new FireStatus(
                FireStatus.Strength.STRONG,
                "clean"
        );
    }

    public FireStatus burnItem(Item item) {

        String raw = (item.fireEffect == null || item.fireEffect.isBlank())
                ? "clean"
                : FireKeyNormalizer.normalize(item.fireEffect);

        FireStatus.Strength strength = FireStatus.Strength.WEAK;
        String effect = raw;

        // Allow item fireEffect to encode strength
        if (raw.startsWith("strong_")) {
            strength = FireStatus.Strength.STRONG;
            effect = raw.substring("strong_".length());
        }
        else if (raw.startsWith("weak_")) {
            strength = FireStatus.Strength.WEAK;
            effect = raw.substring("weak_".length());
        }

        return new FireStatus(strength, effect);
    }
}