package main;

import ui.ExhaustionTextFactory.ExhaustionType;
import logic.VisitTradePricing;
import logic.content.TagSpec;

import java.util.*;
import main.GameConstants;

public class Visit {

    public String type;

    // absolute resource volumes
    public int sellFood = 0;
    public int sellFuel = 0;
    public int buyFood = 0;
    public int buyFuel = 0;

    // per-visit pricing (optional)
    public VisitTradePricing pricing = new VisitTradePricing();

    private final Map<ExhaustionType, String> exhaustionText =
            new EnumMap<>(ExhaustionType.class);

    public List<String> timerStartFireRequired = new ArrayList<>();
    public List<String> timerStartTags = new ArrayList<>();

    public List<String> visitFireRequired = new ArrayList<>();
    public List<String> visitRequiredTags = new ArrayList<>();

    public List<String> fireRequired = new ArrayList<>();
    public List<String> requiredTags = new ArrayList<>();

    public List<String> excludedTags = new ArrayList<>();

    public List<String> dialogue = new ArrayList<>();

    public List<TagSpec> tagsToAdd = new ArrayList<>();

    public List<VisitItem> sells = new ArrayList<>();
    public List<VisitItem> buys = new ArrayList<>();

    public boolean used = false;

    // ---- Absolute calendar window ----
    public Integer minDay;
    public Integer maxDay;

    // ---- Trigger delay window ----
    public Integer delayMinDays;
    public Integer delayMaxDays;

    public Integer firstEligibleDay = null;
    public Integer triggerDay = null;

    public Integer nextScheduledDay = null;

    public Boolean allowScriptedVisits;
    public Boolean allowScheduledVisits;
    public Boolean allowRandomVisits;

    public void setExhaustionText(ExhaustionType type, String text) {
        if (text == null) exhaustionText.remove(type);
        else exhaustionText.put(type, text);
    }

    public String getExhaustionText(ExhaustionType type) {
        return exhaustionText.get(type);
    }

    public boolean isOneShot() {
        return "scripted".equals(type);
    }

    public boolean isScheduled() {
        return "scheduled".equals(type);
    }

    public boolean isWithinDayWindow(int currentDay) {
        if (minDay != null && currentDay < minDay) return false;
        if (maxDay != null && maxDay > 0 && currentDay > maxDay) return false;
        return true;
    }

    public void markFirstEligible(int currentDay) {
        if (firstEligibleDay != null) return;

        firstEligibleDay = currentDay;
        triggerDay = currentDay + randomDelay();
    }

    public boolean isReady(int currentDay) {
        if (triggerDay == null) return false;
        return currentDay >= triggerDay;
    }

    public boolean scheduledReady(int currentDay,
                                  boolean timerStartConditionsMet,
                                  boolean visitConditionsMet) {

        if (timerStartConditionsMet && firstEligibleDay == null) {
            firstEligibleDay = currentDay;
            nextScheduledDay = currentDay + randomDelay();
            return false;
        }

        if (firstEligibleDay == null || nextScheduledDay == null) {
            return false;
        }

        if (currentDay >= nextScheduledDay && visitConditionsMet) {
            nextScheduledDay = currentDay + randomDelay();
            return true;
        }

        return false;
    }

    private int randomDelay() {
        int min = resolveDelayMin();
        int max = resolveDelayMax();
        return new Random().nextInt(max - min + 1) + min;
    }

    public int resolveDelayMin() {
        return delayMinDays != null
                ? delayMinDays
                : GameConstants.SCRIPTED_DEFAULT_MIN_DELAY;
    }

    public int resolveDelayMax() {
        return delayMaxDays != null
                ? delayMaxDays
                : GameConstants.SCRIPTED_DEFAULT_MAX_DELAY;
    }

    public ResolvedTrade resolveTrade(Random rng) {
        ResolvedTrade trade = new ResolvedTrade();

        for (VisitItem v : sells) {
            if (v.isAvailable(rng)) trade.sells.add(v.item);
        }

        for (VisitItem v : buys) {
            if (v.isAvailable(rng)) trade.buys.add(v.item);
        }

        return trade;
    }

    public static class ResolvedTrade {
        public List<String> sells = new ArrayList<>();
        public List<String> buys = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Visit {\n" +
                "  type: " + type + ",\n" +
                "  sellFood: " + sellFood + ",\n" +
                "  sellFuel: " + sellFuel + ",\n" +
                "  buyFood: " + buyFood + ",\n" +
                "  buyFuel: " + buyFuel + ",\n" +
                "  pricing: " + pricing + ",\n" +
                "  timerStartFireRequired: " + timerStartFireRequired + ",\n" +
                "  timerStartTags: " + timerStartTags + ",\n" +
                "  visitFireRequired: " + visitFireRequired + ",\n" +
                "  visitRequiredTags: " + visitRequiredTags + ",\n" +
                "  excludedTags: " + excludedTags + ",\n" +
                "  dialogue: " + dialogue + ",\n" +
                "  tagsToAdd: " + tagsToAdd + ",\n" +
                "  used: " + used + ",\n" +
                "  nextScheduledDay: " + nextScheduledDay + "\n" +
                "}";
    }
}