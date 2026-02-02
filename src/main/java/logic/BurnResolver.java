package logic;

import main.Game;
import main.GameCharacter;
import main.Item;
import main.Visit;

import java.util.*;

public class BurnResolver {

    private final Game game;
    private final Random rng = new Random();

    public BurnResolver(Game game) {
        this.game = game;
    }

    public List<VisitResult> resolveFireMultiple(String fireEffect) {
        List<VisitResult> results = new ArrayList<>();
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

                List<Item> items = new ArrayList<>();
                Visit.ResolvedTrade trade = visit.resolveTrade(rng);
                for (String ref : trade.sells) {
                    Item item = findItem(ref);
                    if (item != null) items.add(item);
                }

                // Pass visit type to VisitResult
                results.add(new VisitResult(character, items, visit.dialogue, fireEffect, visit.type));

                // Apply visit effects
                game.worldTags.addAll(visit.tagsToAdd);
                if (visit.allowScriptedVisits != null) character.allowScriptedVisits = visit.allowScriptedVisits;
                if (visit.allowScheduledVisits != null) character.allowScheduledVisits = visit.allowScheduledVisits;
                if (visit.allowRandomVisits != null) character.allowRandomVisits = visit.allowRandomVisits;
            }
        }

        return results;
    }

    public boolean isVisitTypeAllowed(GameCharacter character, Visit visit) {
        switch (visit.type) {
            case "scripted": return character.allowScriptedVisits;
            case "scheduled": return character.allowScheduledVisits;
            case "random": return character.allowRandomVisits;
            default: return true;
        }
    }

    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }
}
