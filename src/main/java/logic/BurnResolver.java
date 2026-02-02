package logic;

import main.*;

import java.util.Optional;

public class BurnResolver {

    private final Game game;

    public BurnResolver(Game game) {
        this.game = game;
    }

    public Optional<GameCharacter> burnFuel() {

        if (game.player.fuel <= 0) return Optional.empty();

        game.player.fuel--;
        game.burnChosen();

        return resolveFire("strongClean");
    }

    public Optional<GameCharacter> burnItem(Item item) {

        if (!game.player.hasItem(item)) return Optional.empty();

        game.player.removeItem(item);
        game.worldTags.addAll(item.tags);
        game.burnChosen();

        return resolveFire(item.fireEffect);
    }

    private Optional<GameCharacter> resolveFire(String fireEffect) {

        for (GameCharacter character : game.characters) {

            // NEW: once per character per day
            if (game.charactersVisitedToday.contains(character.id)) continue;

            for (Visit visit : character.visits) {

                if (visit.used) continue;
                if (!visit.fireRequired.contains(fireEffect)) continue;
                if (!game.worldTags.containsAll(visit.requiredTags)) continue;

                visit.used = true;
                game.charactersVisitedToday.add(character.id);

                printVisit(character, visit, fireEffect);
                applyVisitEffects(character, visit);

                return Optional.of(character);
            }
        }

        return Optional.empty();
    }

    private void printVisit(GameCharacter character, Visit visit, String fireEffect) {

        System.out.println();
        System.out.println("=== VISIT ===");
        System.out.println("Character: " + character.name);
        System.out.println("Visit type: " + visit.type);
        System.out.println("Fire condition matched: " + fireEffect);

        if (!visit.dialogue.isEmpty()) {
            System.out.println();
            for (String line : visit.dialogue) {
                System.out.println(character.name + ": " + line);
            }
        }

        if (!visit.sells.isEmpty()) {
            System.out.println();
            System.out.println("Sells:");
            for (VisitItem vi : visit.sells) {
                if (vi.rarity != null) {
                    System.out.println("- " + vi.item + " (" + vi.rarity + ")");
                } else {
                    System.out.println("- " + vi.item);
                }
            }
        }

        if (!visit.buys.isEmpty()) {
            System.out.println();
            System.out.println("Buys:");
            for (VisitItem vi : visit.buys) {
                System.out.println("- " + vi.item);
            }
        }

        if (!visit.tagsToAdd.isEmpty()) {
            System.out.println();
            System.out.println("World tags added:");
            for (String tag : visit.tagsToAdd) {
                System.out.println("- " + tag);
            }
        }

        if (visit.goldChange != 0) {
            System.out.println();
            System.out.println("Gold change: " + visit.goldChange);
        }

        System.out.println("================");
        System.out.println();
    }

    private void applyVisitEffects(GameCharacter character, Visit visit) {

        character.gold += visit.goldChange;
        game.worldTags.addAll(visit.tagsToAdd);

        if (visit.allowScriptedVisits != null) {
            character.scriptedVisitsAllowed = visit.allowScriptedVisits;
        }
        if (visit.allowScheduledVisits != null) {
            character.scheduledVisitsAllowed = visit.allowScheduledVisits;
        }
        if (visit.allowRandomVisits != null) {
            character.randomVisitsAllowed = visit.allowRandomVisits;
        }
    }
}
