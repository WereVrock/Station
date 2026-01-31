package logic;

import main.*;

import java.util.Optional;
import main.Character;

public class BurnResolver {

    private final Game game;

    public BurnResolver(Game game) {
        this.game = game;
    }

    public Optional<Character> burnFuel() {

        if (game.player.fuel <= 0) return Optional.empty();

        game.player.fuel--;
        game.burnChosen();

        return resolveFire("strong_clean_fire");
    }

    public Optional<Character> burnItem(Item item) {

        if (!game.player.hasItem(item)) return Optional.empty();

        game.player.removeItem(item);
        game.worldTags.addAll(item.tags);
        game.burnChosen();

        return resolveFire(item.fireEffect);
    }

    private Optional<Character> resolveFire(String fireEffect) {

        for (Character c : game.characters) {
            for (Visit v : c.visits) {

                if (v.used) continue;
                if (!v.fireRequired.contains(fireEffect)) continue;
                if (!game.worldTags.containsAll(v.requiredTags)) continue;

                v.used = true;
                applyEffect(v.effect);
                return Optional.of(c);
            }
        }

        return Optional.empty();
    }

    private void applyEffect(Effect effect) {

        for (String itemName : effect.bringsItems) {
            for (Item i : game.items) {
                if (i.name.equals(itemName)) {
                    game.player.addItem(i);
                }
            }
        }

        game.worldTags.addAll(effect.tagsToAdd);
    }
}
