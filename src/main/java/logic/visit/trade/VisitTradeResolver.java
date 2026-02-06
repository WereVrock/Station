package logic.visit.trade;

import main.Visit;
import logic.VisitTradePricing;

import java.util.Random;

public class VisitTradeResolver {

    private final Random rng;

    public VisitTradeResolver(Random rng) {
        this.rng = rng;
    }

    public Visit.ResolvedTrade resolve(Visit visit) {
        return visit.resolveTrade(rng);
    }

    public VisitTradePricing pricing(Visit visit) {
        return visit.pricing;
    }
}