package logic;

import main.*;
import java.util.*;

public class VisitService {

    private final Game game;
    private final VisitResolver visitResolver;
    private final BurnService burnService;

    private final Queue<VisitResult> pendingVisits = new LinkedList<>();
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

        // 1. scripted
        results.addAll(visitResolver.resolveByType("scripted", fireEffect));

        // 2. scheduled
        results.addAll(visitResolver.resolveByType("scheduled", fireEffect));

        // 3. normal (non-random)
        results.addAll(visitResolver.resolveByType("normal", fireEffect));

        // 4. random if total < 3
        if (results.size() < 3) {
            int want = new Random().nextBoolean() ? 1 : 2;
            want = Math.min(want, GameConstants.VISITS_MAX_PER_DAY - results.size());
            results.addAll(visitResolver.resolveRandomVisits(want));
        }

        pendingVisits.addAll(results);
    }

    private List<VisitResult> drainVisitsForToday() {
        List<VisitResult> result = new ArrayList<>();

        while (!pendingVisits.isEmpty() &&
                result.size() + game.visitsToday < GameConstants.VISITS_MAX_PER_DAY) {
            result.add(pendingVisits.poll());
        }

        game.visitsToday += result.size();
        return result;
    }

    public boolean hasPendingVisits() {
        return !pendingVisits.isEmpty();
    }

    public void nextDay() {
        pendingVisits.clear();
        burnedToday = false;
        game.visitsToday = 0;

        game.day++;
        game.waitingForBurnChoice = true;

        for (GameCharacter c : game.characters) {
            c.visitedToday = false;
        }
    }

    public VisitResolver getResolver() {
        return visitResolver;
    }
}
