package logic;

import main.*;

import java.util.*;

public class BurnResolver {

    private final Game game;
    private final Random rng = new Random();

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

        String fire = normalizeFire(item.fireEffect);
        return resolveFire(fire);
    }

    private String normalizeFire(String fireEffect) {
        if (fireEffect == null || fireEffect.isBlank()) {
            return "weakClean";
        }
        return fireEffect;
    }

    private Optional<GameCharacter> resolveFire(String fireEffect) {

        List<GameCharacter> shuffled = new ArrayList<>(game.characters);
        Collections.shuffle(shuffled);

        for (GameCharacter character : shuffled) {

            if (character.visitedToday) continue;

            for (Visit visit : character.visits) {

                if (visit.used && visit.isOneShot()) continue;
                if (!isVisitTypeAllowed(character, visit)) continue;
                if (!visit.fireRequired.contains(fireEffect)) continue;
                if (!game.worldTags.containsAll(visit.requiredTags)) continue;

                if ("scripted".equals(visit.type)) {
                    visit.markFirstEligible(game.day);
                    if (!visit.isReady(game.day)) continue;
                }

                if (visit.isOneShot()) {
                    visit.used = true;
                }

                character.visitedToday = true;

                setupVisitInventory(character, visit);

                printVisit(character, visit, fireEffect);
                applyVisitEffects(character, visit);

                return Optional.of(character);
            }
        }

        return Optional.empty();
    }

    public boolean isVisitTypeAllowed(GameCharacter character, Visit visit) {

        if ("scripted".equals(visit.type)) return character.allowScriptedVisits;
        if ("scheduled".equals(visit.type)) return character.allowScheduledVisits;
        if ("random".equals(visit.type)) return character.allowRandomVisits;

        return true;
    }

    private void setupVisitInventory(GameCharacter character, Visit visit) {
        character.clearInventory();

        Visit.ResolvedTrade trade = visit.resolveTrade(rng);

        for (String ref : trade.sells) {
            Item item = findItem(ref);
            if (item != null) {
                character.addItem(item);
            }
        }
    }

    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id)) return i;
            if (ref.equals(i.name)) return i;
        }
        return null;
    }

    public void printVisit(GameCharacter character, Visit visit, String fireEffect) {
        printVisitDetailed(character, visit, fireEffect);
    }

    private void printVisitDetailed(GameCharacter character, Visit visit, String fireEffect) {

        System.out.println();
        System.out.println("=== VISIT ===");
        System.out.println("Day: " + game.day);
        System.out.println("Character: " + character.name);
        System.out.println("Type: " + visit.type);
        System.out.println("Fire: " + fireEffect);

        System.out.println();
        for (String line : visit.dialogue) {
            System.out.println(character.name + ": " + line);
        }

        if (!character.inventory.isEmpty()) {
            System.out.println();
            System.out.println(character.name + " offers:");
            for (Item item : character.inventory) {
                System.out.println(" - " + item.name);
            }
        }

        System.out.println("================");
        System.out.println();
    }

    public void applyVisitEffects(GameCharacter character, Visit visit) {

        game.worldTags.addAll(visit.tagsToAdd);

        if (visit.allowScriptedVisits != null) {
            character.allowScriptedVisits = visit.allowScriptedVisits;
        }
        if (visit.allowScheduledVisits != null) {
            character.allowScheduledVisits = visit.allowScheduledVisits;
        }
        if (visit.allowRandomVisits != null) {
            character.allowRandomVisits = visit.allowRandomVisits;
        }
    }
}
