package logic.visit;

import logic.FireKeyNormalizer;
import logic.visit.trade.*;
import logic.visit.resolve.VisitEligibility;
import logic.visit.resolve.VisitMatcher;
import logic.visit.resolve.VisitSelector;
import logic.content.TagSpec;
import main.*;

import java.util.*;
import logic.VisitTradePricing;
import tag.Tag;
import tag.TagManager;

public class VisitResolver {

    private final Game game;
    private final Random rng = new Random();

    private final VisitMatcher matcher;
    private final VisitEligibility eligibility;
    private final VisitSelector selector;
    private final VisitTradeResolver tradeResolver;
    private final ItemLookup itemLookup;
    private final VisitDebugger debugger;

    public VisitResolver(Game game) {
        this.game = game;
        this.matcher = new VisitMatcher();
        this.eligibility = new VisitEligibility();
        this.selector = new VisitSelector();
        this.tradeResolver = new VisitTradeResolver(rng);
        this.itemLookup = new ItemLookup(game);
        this.debugger = new VisitDebugger(game);
    }

    public void setDebugRejectedVisits(boolean debugRejectedVisits) {
        debugger.setDebugRejected(debugRejectedVisits);
    }

    private boolean hasExcludedTag(List<String> excluded) {
        if (excluded == null || excluded.isEmpty()) return false;

        Set<String> present = new HashSet<>();
        for (Tag t : TagManager.view()) {
            present.add(t.getName());
        }

        for (String ex : excluded) {
            if (present.contains(ex)) {
                return true;
            }
        }
        return false;
    }

    public List<VisitResult> resolveByType(String mode, String fireEffect) {

        String normalizedFire = FireKeyNormalizer.normalize(fireEffect);
        List<VisitResult> results = new ArrayList<>();

        for (GameCharacter character : selector.shuffledCharacters(game, rng)) {

            if (character.visitedToday) continue;

            for (Iterator<Visit> it = character.visits.iterator(); it.hasNext();) {
                Visit visit = it.next();
                if (visit.used && visit.isOneShot()) {
                    debugger.debugRejected(character, visit, "One-shot already used", normalizedFire, null);
                    continue;
                }
                if (!eligibility.isAllowed(character, visit)) {
                    debugger.debugRejected(character, visit, "Eligibility blocked", normalizedFire, null);
                    continue;
                }
                if ("normal".equals(mode) && "random".equals(visit.type)) {
                    debugger.debugRejected(character, visit, "Type mismatch", normalizedFire, null);
                    continue;
                }
                if (!"normal".equals(mode) && !mode.equals(visit.type)) {
                    debugger.debugRejected(character, visit, "Type mismatch", normalizedFire, null);
                    continue;
                }
                if (hasExcludedTag(visit.excludedTags)) {
                    debugger.debugRejected(character, visit, "Excluded tag present", normalizedFire, null);
                    continue;
                }
                MatchResult timerMatch = matcher.evaluate(
                        normalizedFire,
                        visit.timerStartFireRequired,
                        visit.timerStartTags,
                        visit.fireRequired,
                        visit.requiredTags
                );
                MatchResult visitMatch = matcher.evaluate(
                        normalizedFire,
                        visit.visitFireRequired,
                        visit.visitRequiredTags,
                        visit.fireRequired,
                        visit.requiredTags
                );
                boolean ok;
                if ("scripted".equals(visit.type)) {
                    if (!visitMatch.success) {
                        debugger.debugRejected(character, visit, "Visit conditions failed", normalizedFire, visitMatch);
                        continue;
                    }
                    visit.markFirstEligible(game.day);
                    ok = visit.isReady(game.day);
                    if (ok) visit.used = true;
                } else if ("scheduled".equals(visit.type)) {
                    ok = visit.scheduledReady(game.day, timerMatch.success, visitMatch.success);
                    if (!ok) {
                        debugger.debugRejected(character, visit, "Scheduled conditions not ready", normalizedFire, visitMatch);
                        continue;
                    }
                } else {
                    ok = visitMatch.success;
                    if (!ok) {
                        debugger.debugRejected(character, visit, "Visit conditions failed", normalizedFire, visitMatch);
                        continue;
                    }
                }
                Visit.ResolvedTrade trade = tradeResolver.resolve(visit);
                VisitTradePricing p = tradeResolver.pricing(visit);
                List<Item> sells = itemLookup.resolve(trade.sells);
                List<Item> buys = itemLookup.resolve(trade.buys);
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
                if (visit.isOneShot()) {
                    it.remove(); 
                }               for (TagSpec spec : visit.tagsToAdd) {
                    TagManager.add(spec.toTag());
                }
                if (visit.allowScriptedVisits != null) {
                    character.allowScriptedVisits = visit.allowScriptedVisits;
                }
                if (visit.allowScheduledVisits != null) {
                    character.allowScheduledVisits = visit.allowScheduledVisits;
                }
                if (visit.allowRandomVisits != null) {
                    character.allowRandomVisits = visit.allowRandomVisits;
                }
                character.visitedToday = true;
                break;
            }
        }

        return results;
    }

    public List<VisitResult> resolveRandomVisits(int count) {

        List<VisitResult> results = new ArrayList<>();

        for (GameCharacter character : selector.shuffledCharacters(game, rng)) {

            if (character.visitedToday) continue;

            for (Visit visit : selector.randomVisits(character)) {

                if (!eligibility.isAllowed(character, visit)) {
                    debugger.debugRejected(character, visit, "Eligibility blocked", "random", null);
                    continue;
                }

                if (hasExcludedTag(visit.excludedTags)) {
                    debugger.debugRejected(character, visit, "Excluded tag present", "random", null);
                    continue;
                }

                Visit.ResolvedTrade trade = tradeResolver.resolve(visit);
                VisitTradePricing p = tradeResolver.pricing(visit);

                VisitResult vr = new VisitResult(
                        character,
                        itemLookup.resolve(trade.sells),
                        itemLookup.resolve(trade.buys),
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

                debugger.debugVisit(
                        character,
                        visit,
                        vr.itemsForSale,
                        vr.itemsWanted,
                        "random"
                );

                results.add(vr);

                character.visitedToday = true;
                break;
            }

            if (results.size() >= count) break;
        }

        return results;
    }

    public MatchResult evaluateDeferred(
            String fireEffect,
            List<String> fireReq,
            List<String> tagReq,
            List<String> legacyFire,
            List<String> legacyTags) {

        return matcher.evaluate(
                fireEffect,
                fireReq,
                tagReq,
                legacyFire,
                legacyTags
        );
    }
}
