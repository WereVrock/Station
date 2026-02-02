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

    // Resolve visits triggered by a fire effect (fuel or item)
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

    // Resolve 1–2 random visits if fewer than 3 visits occurred today
    public List<VisitResult> resolveRandomVisits() {
        List<GameCharacter> eligible = new ArrayList<>(game.characters);
        Collections.shuffle(eligible);
        List<VisitResult> results = new ArrayList<>();

        for (GameCharacter character : eligible) {
            if (character.visitedToday) continue;

            List<Visit> randomVisits = new ArrayList<>();
            for (Visit v : character.visits) {
                if (!"random".equals(v.type)) continue;
                if (!isVisitTypeAllowed(character, v)) continue;
                randomVisits.add(v);
            }
            Collections.shuffle(randomVisits);

            for (Visit visit : randomVisits) {
                character.visitedToday = true;

                List<Item> items = new ArrayList<>();
                Visit.ResolvedTrade trade = visit.resolveTrade(rng);
                for (String ref : trade.sells) {
                    Item item = findItem(ref);
                    if (item != null) items.add(item);
                }

                results.add(new VisitResult(character, items, visit.dialogue, "random", visit.type));
                break; // only one random visit per character per day
            }

            if (results.size() >= 2) break; // 1–2 random visits max
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

    // Essential helper: maps a reference string to an actual Item object
    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }
}
