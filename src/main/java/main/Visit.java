package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Visit {

    public String type;

    public List<String> fireRequired = new ArrayList<>();
    public List<String> requiredTags = new ArrayList<>();

    public List<String> dialogue = new ArrayList<>();
    public List<String> tagsToAdd = new ArrayList<>();

    public List<VisitItem> sells = new ArrayList<>();
    public List<VisitItem> buys = new ArrayList<>();

    // used ONLY for scripted visits
    public boolean used = false;

    public Integer minDay;
    public Integer maxDay;

    public Integer minDays;
    public Integer maxDays;

    public Integer firstEligibleDay = null;
    public Integer triggerDay = null;

    public Boolean allowScriptedVisits;
    public Boolean allowScheduledVisits;
    public Boolean allowRandomVisits;

    public boolean isOneShot() {
        return "scripted".equals(type);
    }

    public void markFirstEligible(int currentDay) {
        if (firstEligibleDay != null) return;

        firstEligibleDay = currentDay;

        int min = resolveMin();
        int max = resolveMax();

        if (min == 0 && max == 0) {
            triggerDay = currentDay;
            return;
        }

        if (min >= 0 && max >= min) {
            int delay = new Random().nextInt(max - min + 1) + min;
            triggerDay = currentDay + delay;
        }
    }

    public boolean isReady(int currentDay) {
        if (triggerDay == null) return false;
        return currentDay >= triggerDay;
    }

    private int resolveMin() {
        if (minDay != null) return minDay;
        if (minDays != null) return minDays;
        return 0;
    }

    private int resolveMax() {
        if (maxDay != null) return maxDay;
        if (maxDays != null) return maxDays;
        return 0;
    }

    public ResolvedTrade resolveTrade(Random rng) {
        ResolvedTrade trade = new ResolvedTrade();

        for (VisitItem v : sells) {
            if (v.isAvailable(rng)) {
                trade.sells.add(v.item);
            }
        }

        for (VisitItem v : buys) {
            if (v.isAvailable(rng)) {
                trade.buys.add(v.item);
            }
        }

        return trade;
    }

    public static class ResolvedTrade {
        public List<String> sells = new ArrayList<>();
        public List<String> buys = new ArrayList<>();
    }
}
