package logic;

import main.Game;
import main.GameCharacter;
import main.Item;
import main.Visit;
import main.GameConstants;

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

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);

                List<Item> itemsForSale = new ArrayList<>();
                for (String ref : trade.sells) {
                    Item item = findItem(ref);
                    if (item != null) itemsForSale.add(item);
                }

                List<Item> itemsWanted = new ArrayList<>();
                for (String ref : trade.buys) {
                    Item item = findItem(ref);
                    if (item != null) itemsWanted.add(item);
                }

                results.add(new VisitResult(
                        character,
                        itemsForSale,
                        itemsWanted,
                        visit.dialogue,
                        fireEffect,
                        visit.type
                ));

                game.worldTags.addAll(visit.tagsToAdd);
                if (visit.allowScriptedVisits != null) character.allowScriptedVisits = visit.allowScriptedVisits;
                if (visit.allowScheduledVisits != null) character.allowScheduledVisits = visit.allowScheduledVisits;
                if (visit.allowRandomVisits != null) character.allowRandomVisits = visit.allowRandomVisits;
            }
        }

        return results;
    }

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

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);

                List<Item> itemsForSale = new ArrayList<>();
                for (String ref : trade.sells) {
                    Item item = findItem(ref);
                    if (item != null) itemsForSale.add(item);
                }

                List<Item> itemsWanted = new ArrayList<>();
                for (String ref : trade.buys) {
                    Item item = findItem(ref);
                    if (item != null) itemsWanted.add(item);
                }

                results.add(new VisitResult(
                        character,
                        itemsForSale,
                        itemsWanted,
                        visit.dialogue,
                        "random",
                        visit.type
                ));
                break;
            }

            if (results.size() >= GameConstants.VISITS_RANDOM_MAX) break;
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