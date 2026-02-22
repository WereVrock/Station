package logic;

import main.FireStatus;
import main.Item;
import main.Main;
import tag.Tag;
import tag.TagManager;

public class BurnService {

    public FireStatus burnFuel() {

        if (Main.game.player.fuel <= 0) {
            return Main.game.getFireStatus();
        }

        Main.game.player.fuel -= main.GameConstants.FUEL_BURN_COST;

        FireStatus fireStatus = new FireStatus(
                FireStatus.Strength.STRONG,
                "clean"
        );

        Main.game.setFireStatus(fireStatus);
        Main.game.burnChosen();

        return fireStatus;
    }

    public FireStatus burnItem(Item item) {

        if (item == null || !Main.game.player.hasItem(item)) {
            return Main.game.getFireStatus();
        }

        Main.game.player.removeItem(item);

        if (item.tags != null) {
            for (String tagName : item.tags) {
                TagManager.add(new Tag(tagName));
            }
        }

        FireStatus.Strength strength =
                item.fireStrength != null
                        ? item.fireStrength
                        : FireStatus.Strength.WEAK;

        String effect =
                (item.fireEffect == null || item.fireEffect.isBlank())
                        ? "clean"
                        : item.fireEffect;

        FireStatus fireStatus = new FireStatus(strength, effect);

        Main.game.setFireStatus(fireStatus);
        Main.game.burnChosen();

        return fireStatus;
    }
}