// ===== BurnResolver.java =====
package logic;

import main.*;
import java.util.*;

public class BurnResolver {

    private final Game game;
    private final Random rng = new Random();

    public BurnResolver(Game game) {
        this.game = game;
    }

    public List<GameCharacter> resolveFireMultiple(String fireEffect) {
        List<GameCharacter> result = new ArrayList<>();
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

                if (visit.isOneShot()) visit.used = true;

                character.visitedToday = true;
                setupVisitInventory(character, visit);
                printVisit(character, visit, fireEffect);
                applyVisitEffects(character, visit);

                result.add(character);
            }
        }

        return result;
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
            if (item != null) character.addItem(item);
        }
    }

    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }

    private void printVisit(GameCharacter character, Visit visit, String fireEffect) {
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

    private void applyVisitEffects(GameCharacter character, Visit visit) {
        game.worldTags.addAll(visit.tagsToAdd);
        if (visit.allowScriptedVisits != null) character.allowScriptedVisits = visit.allowScriptedVisits;
        if (visit.allowScheduledVisits != null) character.allowScheduledVisits = visit.allowScheduledVisits;
        if (visit.allowRandomVisits != null) character.allowRandomVisits = visit.allowRandomVisits;
    }
}
