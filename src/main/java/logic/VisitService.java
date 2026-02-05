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

        enqueueResolvedVisits(fireStatus.toString());
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

        enqueueResolvedVisits(fireStatus.toString());
        return drainVisitsForToday();
    }

    private void enqueueResolvedVisits(String fireEffect) {
        List<VisitResult> resolved =
                visitResolver.resolveVisitsByFire(fireEffect);

        pendingVisits.addAll(resolved);
        resolveRandomVisitsIfNeeded();
    }

    private void resolveRandomVisitsIfNeeded() {
        int visitsToday = game.visitsToday + pendingVisits.size();
        if (visitsToday >= GameConstants.VISITS_RANDOM_TRIGGER_THRESHOLD) return;

        List<VisitResult> randoms = visitResolver.resolveRandomVisits();
        for (VisitResult r : randoms) {
            if (pendingVisits.size() + game.visitsToday >= GameConstants.VISITS_MAX_PER_DAY) break;
            pendingVisits.add(r);
        }
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