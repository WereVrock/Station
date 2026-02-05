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
        if (item == null || !item.burnable) {
            return new FireStatus(FireStatus.Strength.WEAK, "clean");
        }

        FireStatus.Strength strength =
                item.fireStrength != null
                        ? item.fireStrength
                        : FireStatus.Strength.WEAK;

        String effect =
                (item.fireEffect == null || item.fireEffect.isBlank())
                        ? "clean"
                        : item.fireEffect;

        return new FireStatus(strength, effect);
    }
}