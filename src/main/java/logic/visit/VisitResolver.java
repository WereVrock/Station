package logic.visit;

import main.VisitResult;
import logic.FireKeyNormalizer;
import logic.visit.trade.*;
import logic.visit.resolve.VisitEligibility;
import logic.visit.resolve.VisitMatcher;
import logic.visit.resolve.VisitSelector;
import content.TagSpec;
import content.logic.triggers.GameEvent;
import content.logic.triggers.engine.TriggerEngine;
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
            if (present.contains(ex)) return true;
        }
        return false;
    }

    public List<VisitResult> resolveByType(String mode, String fireEffect) {

        List<VisitResult> results = new ArrayList<>();

        if ("scripted".equals(mode)) {
            results.addAll(resolveByTypeOrdered("scripted", fireEffect));
        }
        else if ("scheduled".equals(mode)) {
            results.addAll(resolveByTypeOrdered("scripted", fireEffect));
            if (results.isEmpty()) {
                results.addAll(resolveByTypeOrdered("scheduled", fireEffect));
            }
        }
        else {
            results.addAll(resolveByTypeOrdered("scripted", fireEffect));
            results.addAll(resolveByTypeOrdered("scheduled", fireEffect));
            if (results.isEmpty()) {
                results.addAll(resolveRandomVisits(Integer.MAX_VALUE));
            }
        }

        return results;
    }

    private List<VisitResult> resolveByTypeOrdered(String mode, String fireEffect) {

        String normalizedFire = FireKeyNormalizer.normalize(fireEffect);
        List<VisitResult> results = new ArrayList<>();

        for (GameCharacter character : game.characters) {

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

                if (!mode.equals(visit.type)) continue;

                if (hasExcludedTag(visit.excludedTags)) {
                    debugger.debugRejected(character, visit, "Excluded tag present", normalizedFire, null);
                    continue;
                }

                if (!visit.isWithinDayWindow(game.day)) {
                    debugger.debugRejected(character, visit, "Outside day window", normalizedFire, null);
                    continue;
                }

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

                    if (!ok) continue;

                    visit.used = true;

                } else {

                    ok = visit.scheduledReady(game.day, true, visitMatch.success);

                    if (!ok) {
                        debugger.debugRejected(character, visit, "Scheduled conditions not ready", normalizedFire, visitMatch);
                        continue;
                    }
                }

                Visit.ResolvedTrade trade = tradeResolver.resolve(visit);
                VisitTradePricing p = tradeResolver.pricing(visit);

                VisitResult vr = new VisitResult(
                        character,
                        itemLookup.resolve(trade.sells),
                        itemLookup.resolve(trade.buys),
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
                        p.resolveBuyFuel(),
                        visit
                );

                debugger.debugVisit(character, visit, vr.itemsForSale, vr.itemsWanted, normalizedFire);
                results.add(vr);

                VisitTriggerContext context =
                        new VisitTriggerContext(visit);

                TriggerEngine.evaluate(
                        visit.runtimeTriggers,
                        GameEvent.VISIT_START.toString(),
                        context
                );

                if (visit.isOneShot()) it.remove();

                for (TagSpec spec : visit.tagsToAdd) {
                    TagManager.add(spec.toTag());
                }

                if (visit.allowScriptedVisits != null) character.allowScriptedVisits = visit.allowScriptedVisits;
                if (visit.allowScheduledVisits != null) character.allowScheduledVisits = visit.allowScheduledVisits;
                if (visit.allowRandomVisits != null) character.allowRandomVisits = visit.allowRandomVisits;

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

                if (!visit.isWithinDayWindow(game.day)) {
                    debugger.debugRejected(character, visit, "Outside day window", "random", null);
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
                        p.resolveBuyFuel(),
                        visit
                );

                debugger.debugVisit(character, visit, vr.itemsForSale, vr.itemsWanted, "random");

                results.add(vr);
                character.visitedToday = true;
                break;
            }

            if (results.size() >= count) break;
        }

        return results;
    }

    public MatchResult evaluateDeferred(
            List<String> fireReq,
            List<String> tagReq,
            List<String> legacyFire,
            List<String> legacyTags) {

        return matcher.evaluate(
                null,
                fireReq,
                tagReq,
                legacyFire,
                legacyTags
        );
    }
}