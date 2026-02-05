package logic;

import main.*;
import java.util.*;

public class VisitService {

    private final Game game;
    private final VisitResolver visitResolver;
    private final BurnService burnService;

    private final Queue<VisitResult> pendingVisits = new LinkedList<>();
    private final Queue<DeferredVisit> deferredVisits = new LinkedList<>();

    private boolean burnedToday = false;

    public VisitService(Game game) {
        this.game = game;
        this.visitResolver = new VisitResolver(game);
        this.burnService = new BurnService();
    }

    public List<VisitResult> burnFuel() {
        if (burnedToday || game.player.fuel <= 0) return Collections.emptyList();

        burnedToday = true;
        game.player.fuel -= GameConstants.FUEL_BURN_COST;
        game.burnChosen();

        FireStatus fireStatus = burnService.burnFuel();
        game.setFireStatus(fireStatus);

        enqueueResolvedVisits(FireVisitKey.from(fireStatus).legacyKey());
        return drainVisitsForToday();
    }

    public List<VisitResult> burnItem(Item item) {
        if (burnedToday || item == null || !game.player.hasItem(item)) {
            return Collections.emptyList();
        }

        burnedToday = true;
        game.player.removeItem(item);
        game.worldTags.addAll(item.tags);
        game.burnChosen();

        FireStatus fireStatus = burnService.burnItem(item);
        game.setFireStatus(fireStatus);

        enqueueResolvedVisits(FireVisitKey.from(fireStatus).legacyKey());
        return drainVisitsForToday();
    }

    private void enqueueResolvedVisits(String fireEffect) {

        List<VisitResult> results = new ArrayList<>();

        results.addAll(visitResolver.resolveByType("scripted", fireEffect));
        results.addAll(visitResolver.resolveByType("scheduled", fireEffect));
        results.addAll(visitResolver.resolveByType("normal", fireEffect));

        if (results.size() < 3) {
            int want = new Random().nextBoolean() ? 1 : 2;
            want = Math.min(want, GameConstants.VISITS_MAX_PER_DAY - results.size());
            results.addAll(visitResolver.resolveRandomVisits(want));
        }

        pendingVisits.addAll(results);
    }

    private List<VisitResult> drainVisitsForToday() {

        List<VisitResult> result = new ArrayList<>();

        while (!pendingVisits.isEmpty()) {

            if (result.size() + game.visitsToday < GameConstants.VISITS_MAX_PER_DAY) {
                result.add(pendingVisits.poll());
            } else {
                VisitResult vr = pendingVisits.poll();
                deferredVisits.add(new DeferredVisit(
                        vr.character,
                        findVisit(vr),
                        vr.fireEffect,
                        game.day
                ));
            }
        }

        game.visitsToday += result.size();
        return result;
    }

    public boolean hasPendingVisits() {
        return !pendingVisits.isEmpty() || !deferredVisits.isEmpty();
    }

    public void nextDay() {

        burnedToday = false;
        game.visitsToday = 0;

        game.day++;
        game.waitingForBurnChoice = true;

        for (GameCharacter c : game.characters) {
            c.visitedToday = false;
        }

        requeueDeferred();
    }

    private void requeueDeferred() {

        int size = deferredVisits.size();

        for (int i = 0; i < size; i++) {
            DeferredVisit dv = deferredVisits.poll();

            if (dv.stillValid(game, visitResolver)) {

                Visit.ResolvedTrade trade = dv.visit.resolveTrade(new Random());

                List<Item> sells = lookUpItems(trade.sells);
                List<Item> buys = lookUpItems(trade.buys);

                VisitTradePricing p = dv.visit.pricing;

                VisitResult vr = new VisitResult(
                        dv.character,
                        sells,
                        buys,
                        dv.visit.dialogue,
                        dv.fireEffect,
                        dv.visit.type,
                        dv.visit.sellFood,
                        dv.visit.sellFuel,
                        dv.visit.buyFood,
                        dv.visit.buyFuel,
                        p.resolveSellFood(),
                        p.resolveSellFuel(),
                        p.resolveBuyFood(),
                        p.resolveBuyFuel()
                );

                pendingVisits.add(vr);
            }
        }
    }

    private Visit findVisit(VisitResult vr) {
        for (Visit v : vr.character.visits) {
            if (v.dialogue == vr.dialogue) return v;
        }
        return null;
    }

    private List<Item> lookUpItems(List<String> refs) {
        List<Item> items = new ArrayList<>();
        for (String ref : refs) {
            Item item = findItem(ref);
            if (item != null) items.add(item);
        }
        return items;
    }

    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }

    public VisitResolver getResolver() {
        return visitResolver;
    }
}
