package logic;

import main.Game;
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

    public List<VisitResult> resolveByType(String mode, String fireEffect) {

        String normalizedFire = FireKeyNormalizer.normalize(fireEffect);
        List<VisitResult> results = new ArrayList<>();

        List<GameCharacter> shuffled = new ArrayList<>(game.characters);
        Collections.shuffle(shuffled);

        for (GameCharacter character : shuffled) {

            if (character.visitedToday) continue;

            for (Visit visit : character.visits) {

                if (visit.used && visit.isOneShot()) continue;

                if ("normal".equals(mode) && "random".equals(visit.type)) continue;
                if (!"normal".equals(mode) && !mode.equals(visit.type)) continue;

                if (!isVisitTypeAllowed(character, visit)) continue;

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

                boolean ok = false;

                if ("scripted".equals(visit.type)) {
                    if (!visitMatch.success) continue;
                    visit.markFirstEligible(game.day);
                    ok = visit.isReady(game.day);
                    if (ok) visit.used = true;
                }

                else if ("scheduled".equals(visit.type)) {
                    ok = visit.scheduledReady(game.day, timerMatch.success, visitMatch.success);
                }

                else {
                    ok = visitMatch.success;
                }

                if (!ok) continue;

                character.visitedToday = true;

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);
                List<Item> sells = lookUpItems(trade.sells);
                List<Item> buys = lookUpItems(trade.buys);

                VisitTradePricing p = visit.pricing;

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
                        visit.buyFuel,
                        p.resolveSellFood(),
                        p.resolveSellFuel(),
                        p.resolveBuyFood(),
                        p.resolveBuyFuel()
                );

                debugger.debugVisit(character, visit, sells, buys, normalizedFire);
                results.add(vr);

                game.worldTags.addAll(visit.tagsToAdd);
                if (visit.allowScriptedVisits != null) character.allowScriptedVisits = visit.allowScriptedVisits;
                if (visit.allowScheduledVisits != null) character.allowScheduledVisits = visit.allowScheduledVisits;
                if (visit.allowRandomVisits != null) character.allowRandomVisits = visit.allowRandomVisits;

                break;
            }
        }

        return results;
    }

    public List<VisitResult> resolveRandomVisits(int count) {

        List<GameCharacter> eligible = new ArrayList<>(game.characters);
        Collections.shuffle(eligible);
        List<VisitResult> results = new ArrayList<>();

        for (GameCharacter character : eligible) {

            if (character.visitedToday) continue;

            List<Visit> randomVisits = new ArrayList<>();
            for (Visit v : character.visits) {
                if ("random".equals(v.type) && isVisitTypeAllowed(character, v)) {
                    randomVisits.add(v);
                }
            }

            Collections.shuffle(randomVisits);

            for (Visit visit : randomVisits) {

                character.visitedToday = true;

                Visit.ResolvedTrade trade = visit.resolveTrade(rng);
                List<Item> sells = lookUpItems(trade.sells);
                List<Item> buys = lookUpItems(trade.buys);

                VisitTradePricing p = visit.pricing;

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
                        visit.buyFuel,
                        p.resolveSellFood(),
                        p.resolveSellFuel(),
                        p.resolveBuyFood(),
                        p.resolveBuyFuel()
                );

                debugger.debugVisit(character, visit, sells, buys, "random");
                results.add(vr);
                break;
            }

            if (results.size() >= count) break;
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

    public MatchResult evaluateDeferred(String fireEffect,
                                        Set<String> worldTags,
                                        List<String> fireReq,
                                        List<String> tagReq,
                                        List<String> legacyFire,
                                        List<String> legacyTags) {

        return evaluateVisitConditions(
                fireEffect,
                worldTags,
                fireReq,
                tagReq,
                legacyFire,
                legacyTags
        );
    }

    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }
}
