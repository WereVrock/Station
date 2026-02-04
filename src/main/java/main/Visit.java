package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Visit {

    public String type;

    // NEW condition split
    public List<String> timerStartFireRequired = new ArrayList<>();
    public List<String> timerStartTags = new ArrayList<>();

    public List<String> visitFireRequired = new ArrayList<>();
    public List<String> visitRequiredTags = new ArrayList<>();

    // legacy support (kept)
    public List<String> fireRequired = new ArrayList<>();
    public List<String> requiredTags = new ArrayList<>();

    public List<String> dialogue = new ArrayList<>();
    public List<String> tagsToAdd = new ArrayList<>();

    public List<VisitItem> sells = new ArrayList<>();
    public List<VisitItem> buys = new ArrayList<>();

    public boolean used = false;

    public Integer minDays;
    public Integer maxDays;

    public Integer firstEligibleDay = null;
    public Integer triggerDay = null;

    public Integer nextScheduledDay = null;

    public Boolean allowScriptedVisits;
    public Boolean allowScheduledVisits;
    public Boolean allowRandomVisits;

    public boolean isOneShot() {
        return "scripted".equals(type);
    }

    public boolean isScheduled() {
        return "scheduled".equals(type);
    }

    public void markFirstEligible(int currentDay) {
        if (firstEligibleDay != null) return;

        firstEligibleDay = currentDay;

        int delay = randomDelay();
        triggerDay = currentDay + delay;
    }

    public boolean isReady(int currentDay) {
        if (triggerDay == null) return false;
        return currentDay >= triggerDay;
    }

    public boolean scheduledReady(int currentDay,
                                  boolean timerStartConditionsMet,
                                  boolean visitConditionsMet) {

        // Start timer once
        if (timerStartConditionsMet && firstEligibleDay == null) {
            firstEligibleDay = currentDay;
            nextScheduledDay = currentDay + randomDelay();
            return false;
        }

        if (firstEligibleDay == null || nextScheduledDay == null) {
            return false;
        }

        // Timer matured but wait for visit conditions
        if (currentDay >= nextScheduledDay && visitConditionsMet) {
            nextScheduledDay = currentDay + randomDelay();
            return true;
        }

        return false;
    }

    private int randomDelay() {
        int min = resolveMin();
        int max = resolveMax();
        return new Random().nextInt(max - min + 1) + min;
    }

    private int resolveMin() {
        return minDays != null ? minDays : GameConstants.SCRIPTED_DEFAULT_MIN_DELAY;
    }

    private int resolveMax() {
        return maxDays != null ? maxDays : GameConstants.SCRIPTED_DEFAULT_MAX_DELAY;
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
        StringBuilder sb = new StringBuilder();

        sb.append("Visit {\n");
        sb.append("  type: ").append(type).append(",\n");
        sb.append("  timerStartFireRequired: ").append(timerStartFireRequired).append(",\n");
        sb.append("  timerStartTags: ").append(timerStartTags).append(",\n");
        sb.append("  visitFireRequired: ").append(visitFireRequired).append(",\n");
        sb.append("  visitRequiredTags: ").append(visitRequiredTags).append(",\n");

        sb.append("  dialogue: ").append(dialogue).append(",\n");
        sb.append("  tagsToAdd: ").append(tagsToAdd).append(",\n");

        sb.append("  used: ").append(used).append(",\n");
        sb.append("  nextScheduledDay: ").append(nextScheduledDay).append("\n");
        sb.append("}");

        return sb.toString();
    }
}