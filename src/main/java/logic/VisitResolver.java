package logic;

import main.*;
import java.util.*;

public class VisitResolver {

    private final Game game;
    private final Random rng = new Random();
    private final VisitDebugger debugger;

    public VisitResolver(Game game) {
        this.game = game;
        this.debugger = new VisitDebugger(game);
    }

    public void setDebugRejectedVisits(boolean debugRejectedVisits) {
        this.debugger.setDebugRejected(debugRejectedVisits);
    }

    public List<VisitResult> resolveVisitsByFire(String fireEffect) {
        String normalizedFire = FireKeyNormalizer.normalize(fireEffect);
        List<VisitResult> results = new ArrayList<>();
        List<GameCharacter> shuffled = new ArrayList<>(game.characters);
        Collections.shuffle(shuffled);

        for (GameCharacter character : shuffled) {
            if (character.visitedToday) {
                debugger.debugRejected(character, null, "Character already visited today", normalizedFire, null);
                continue;
            }

            for (Visit visit : character.visits) {

                if (visit.isOneShot() && visit.used) {
                    debugger.debugRejected(character, visit, "One shot visit already used", normalizedFire, null);
                    continue;
                }

                if (!isVisitTypeAllowed(character, visit)) {
                    debugger.debugRejected(character, visit, "Visit type not allowed for character", normalizedFire, null);
                    continue;
                }

                MatchResult timerMatch = evaluateVisitConditions(
                        normalizedFire,
                        game.worldTags,
                        visit.timerStartFireRequired,
                        visit.timerStartTags,
                        visit.fireRequired,
                        visit.requiredTags
                );

                MatchResult visitMatch = evaluateVisitConditions(
                        normalizedFire,
                        game.worldTags,
                        visit.visitFireRequired,
                        visit.visitRequiredTags,
                        visit.fireRequired,
                        visit.requiredTags
                );

                if ("scripted".equals(visit.type)) {
                    if (!visitMatch.success) {
                        debugger.debugRejected(character, visit, "Scripted visit conditions not met", normalizedFire, visitMatch);
                        continue;
                    }
                    visit.markFirstEligible(game.day);
                    if (!visit.isReady(game.day)) {
                        debugger.debugRejected(character, visit, "Scripted visit timer not ready", normalizedFire, null);
                        continue;
                    }
                    visit.used = true;

                } else if ("scheduled".equals(visit.type)) {
                    if (!visit.scheduledReady(game.day, timerMatch.success, visitMatch.success)) {
                        String reason = "Scheduled visit not ready "
                                + "(timerStart=" + timerMatch.success
                                + ", visitConditions=" + visitMatch.success + ")";
                        debugger.debugRejected(character, visit, reason, normalizedFire, visitMatch);
                        continue;
                    }

                } else {
                    if (!visitMatch.success) {
                        debugger.debugRejected(character, visit, "Visit conditions not met", normalizedFire, visitMatch);
                        continue;
                    }
                }

                character.visitedToday = true;

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);
                List<Item> sells = lookUpItems(trade.sells);
                List<Item> buys = lookUpItems(trade.buys);

                VisitResult vr = new VisitResult(
                        character,
                        sells,
                        buys,
                        visit.dialogue,
                        normalizedFire,
                        visit.type,
                        visit.sellFood,
                        visit.sellFuel,
                        visit.buyFood,
                        visit.buyFuel
                );

                debugger.debugVisit(character, visit, sells, buys, normalizedFire);
                results.add(vr);

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
            if (character.visitedToday) {
                debugger.debugRejected(character, null, "Character already visited today (random)", "random", null);
                continue;
            }

            List<Visit> randomVisits = new ArrayList<>();
            for (Visit v : character.visits) {
                if (!"random".equals(v.type)) continue;
                if (!isVisitTypeAllowed(character, v)) {
                    debugger.debugRejected(character, v, "Random visit type not allowed", "random", null);
                    continue;
                }
                randomVisits.add(v);
            }

            Collections.shuffle(randomVisits);
            for (Visit visit : randomVisits) {
                character.visitedToday = true;

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);
                List<Item> sells = lookUpItems(trade.sells);
                List<Item> buys = lookUpItems(trade.buys);

                VisitResult vr = new VisitResult(
                        character,
                        sells,
                        buys,
                        visit.dialogue,
                        "random",
                        visit.type,
                        visit.sellFood,
                        visit.sellFuel,
                        visit.buyFood,
                        visit.buyFuel
                );

                debugger.debugVisit(character, visit, sells, buys, "random");
                results.add(vr);
                break;
            }

            if (results.size() >= GameConstants.VISITS_RANDOM_MAX) break;
        }

        return results;
    }

    private MatchResult evaluateVisitConditions(String fireEffect,
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

    private List<Item> lookUpItems(List<String> refs) {
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
}
