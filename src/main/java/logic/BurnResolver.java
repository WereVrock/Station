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

    private boolean debugRejectedVisits = false;

    public BurnResolver(Game game) {
        this.game = game;
    }

    public void setDebugRejectedVisits(boolean debugRejectedVisits) {
        this.debugRejectedVisits = debugRejectedVisits;
    }

    public List<VisitResult> resolveFireMultiple(String fireEffect) {

        String normalizedFire = FireKeyNormalizer.normalize(fireEffect);

        List<VisitResult> results = new ArrayList<>();
        List<GameCharacter> shuffled = new ArrayList<>(game.characters);
        Collections.shuffle(shuffled);

        for (GameCharacter character : shuffled) {

            if (character.visitedToday) {
                debugRejected(character, null, "Character already visited today", normalizedFire, null);
                continue;
            }

            for (Visit visit : character.visits) {

                if (visit.isOneShot() && visit.used) {
                    debugRejected(character, visit, "One shot visit already used", normalizedFire, null);
                    continue;
                }

                if (!isVisitTypeAllowed(character, visit)) {
                    debugRejected(character, visit, "Visit type not allowed for character", normalizedFire, null);
                    continue;
                }

                MatchResult timerMatch = evaluateMatch(
                        normalizedFire,
                        game.worldTags,
                        visit.timerStartFireRequired,
                        visit.timerStartTags,
                        visit.fireRequired,
                        visit.requiredTags
                );

                MatchResult visitMatch = evaluateMatch(
                        normalizedFire,
                        game.worldTags,
                        visit.visitFireRequired,
                        visit.visitRequiredTags,
                        visit.fireRequired,
                        visit.requiredTags
                );

                if ("scripted".equals(visit.type)) {

                    if (!visitMatch.success) {
                        debugRejected(character, visit,
                                "Scripted visit conditions not met",
                                normalizedFire,
                                visitMatch);
                        continue;
                    }

                    visit.markFirstEligible(game.day);

                    if (!visit.isReady(game.day)) {
                        debugRejected(character, visit,
                                "Scripted visit timer not ready",
                                normalizedFire,
                                null);
                        continue;
                    }

                    visit.used = true;
                }

                else if ("scheduled".equals(visit.type)) {

                    if (!visit.scheduledReady(
                            game.day,
                            timerMatch.success,
                            visitMatch.success
                    )) {

                        String reason = "Scheduled visit not ready "
                                + "(timerStart=" + timerMatch.success
                                + ", visitConditions=" + visitMatch.success + ")";

                        debugRejected(character, visit, reason, normalizedFire, visitMatch);
                        continue;
                    }
                }

                else {
                    if (!visitMatch.success) {
                        debugRejected(character, visit,
                                "Visit conditions not met",
                                normalizedFire,
                                visitMatch);
                        continue;
                    }
                }

                character.visitedToday = true;

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);

                List<Item> sells = resolveItems(trade.sells);
                List<Item> buys = resolveItems(trade.buys);

                VisitResult vr = new VisitResult(
                        character,
                        sells,
                        buys,
                        visit.dialogue,
                        normalizedFire,
                        visit.type
                );

                debugVisit(character, visit, sells, buys, normalizedFire);

                results.add(vr);

                game.worldTags.addAll(visit.tagsToAdd);

                if (visit.allowScriptedVisits != null)
                    character.allowScriptedVisits = visit.allowScriptedVisits;

                if (visit.allowScheduledVisits != null)
                    character.allowScheduledVisits = visit.allowScheduledVisits;

                if (visit.allowRandomVisits != null)
                    character.allowRandomVisits = visit.allowRandomVisits;
            }
        }

        return results;
    }

    public List<VisitResult> resolveRandomVisits() {

        List<GameCharacter> eligible = new ArrayList<>(game.characters);
        Collections.shuffle(eligible);
        List<VisitResult> results = new ArrayList<>();

        for (GameCharacter character : eligible) {

            if (character.visitedToday) {
                debugRejected(character, null, "Character already visited today (random)", "random", null);
                continue;
            }

            List<Visit> randomVisits = new ArrayList<>();

            for (Visit v : character.visits) {
                if (!"random".equals(v.type)) continue;

                if (!isVisitTypeAllowed(character, v)) {
                    debugRejected(character, v, "Random visit type not allowed", "random", null);
                    continue;
                }

                randomVisits.add(v);
            }

            Collections.shuffle(randomVisits);

            for (Visit visit : randomVisits) {

                character.visitedToday = true;

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);

                List<Item> sells = resolveItems(trade.sells);
                List<Item> buys = resolveItems(trade.buys);

                VisitResult vr = new VisitResult(
                        character,
                        sells,
                        buys,
                        visit.dialogue,
                        "random",
                        visit.type
                );

                debugVisit(character, visit, sells, buys, "random");

                results.add(vr);
                break;
            }

            if (results.size() >= GameConstants.VISITS_RANDOM_MAX) break;
        }

        return results;
    }

    private MatchResult evaluateMatch(String fireEffect,
                                      Set<String> worldTags,
                                      List<String> fireReq,
                                      List<String> tagReq,
                                      List<String> legacyFire,
                                      List<String> legacyTags) {

        List<String> sourceFire = fireReq.isEmpty() ? legacyFire : fireReq;
        List<String> sourceTags = tagReq.isEmpty() ? legacyTags : tagReq;

        List<String> normalizedRequiredFire = new ArrayList<>();
        for (String fire : sourceFire) {
            normalizedRequiredFire.add(FireKeyNormalizer.normalize(fire));
        }

        MatchResult result = new MatchResult();
        result.requiredFire = new ArrayList<>(normalizedRequiredFire);
        result.requiredTags = new ArrayList<>(sourceTags);
        result.actualFire = fireEffect;
        result.actualTags = new HashSet<>(worldTags);

        result.fireOk = normalizedRequiredFire.isEmpty() || normalizedRequiredFire.contains(fireEffect);
        result.tagsOk = worldTags.containsAll(sourceTags);
        result.success = result.fireOk && result.tagsOk;

        return result;
    }

    private void debugVisit(GameCharacter character,
                            Visit visit,
                            List<Item> sells,
                            List<Item> buys,
                            String fireEffect) {

        System.out.println("===== VISIT TRIGGERED =====");
        System.out.println("Character: " + character.name);
        System.out.println("Type: " + visit.type);
        System.out.println("Fire Effect: " + fireEffect);

        System.out.println("Items For Sale:");
        if (sells.isEmpty()) System.out.println("  none");
        else sells.forEach(i -> System.out.println("  " + i.name));

        System.out.println("Items Wanted:");
        if (buys.isEmpty()) System.out.println("  none");
        else buys.forEach(i -> System.out.println("  " + i.name));

        System.out.println("===========================");
    }

    private void debugRejected(GameCharacter character,
                               Visit visit,
                               String reason,
                               String fireEffect,
                               MatchResult match) {

        if (!debugRejectedVisits) return;

        System.out.println("----- VISIT REJECTED -----");
        System.out.println("Character: " + character.name);

        if (visit != null) {
            System.out.println("Type: " + visit.type);
        }

        System.out.println("Reason: " + reason);
        System.out.println("Current Fire: " + fireEffect);
        System.out.println("World Tags: " + game.worldTags);

        if (match != null) {
            System.out.println("Required Fire: " + match.requiredFire);
            System.out.println("Required Tags: " + match.requiredTags);
            System.out.println("Fire Match: " + match.fireOk);
            System.out.println("Tag Match: " + match.tagsOk);
        }

        System.out.println("--------------------------");
    }

    private List<Item> resolveItems(List<String> refs) {
        List<Item> items = new ArrayList<>();
        for (String ref : refs) {
            Item item = findItem(ref);
            if (item != null) items.add(item);
        }
        return items;
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

    private static class MatchResult {
        boolean success;
        boolean fireOk;
        boolean tagsOk;

        List<String> requiredFire;
        List<String> requiredTags;

        String actualFire;
        Set<String> actualTags;
    }
}