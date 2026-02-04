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

    public boolean used = false;

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
        return minDays != null ? minDays : GameConstants.SCRIPTED_DEFAULT_MIN_DELAY;
    }

    private int resolveMax() {
        return maxDays != null ? maxDays : GameConstants.SCRIPTED_DEFAULT_MAX_DELAY;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Visit {\n");
        sb.append("  type: ").append(type).append(",\n");
        sb.append("  fireRequired: ").append(fireRequired).append(",\n");
        sb.append("  requiredTags: ").append(requiredTags).append(",\n");
        sb.append("  dialogue: ").append(dialogue).append(",\n");
        sb.append("  tagsToAdd: ").append(tagsToAdd).append(",\n");

        sb.append("  sells: [");
        for (int i = 0; i < sells.size(); i++) {
            sb.append(sells.get(i));
            if (i < sells.size() - 1) sb.append(", ");
        }
        sb.append("],\n");

        sb.append("  buys: [");
        for (int i = 0; i < buys.size(); i++) {
            sb.append(buys.get(i));
            if (i < buys.size() - 1) sb.append(", ");
        }
        sb.append("],\n");

        sb.append("  used: ").append(used).append(",\n");
        sb.append("  minDays: ").append(minDays).append(", maxDays: ").append(maxDays).append(",\n");
        sb.append("  firstEligibleDay: ").append(firstEligibleDay).append(", triggerDay: ").append(triggerDay).append(",\n");
        sb.append("  allowScriptedVisits: ").append(allowScriptedVisits).append(",\n");
        sb.append("  allowScheduledVisits: ").append(allowScheduledVisits).append(",\n");
        sb.append("  allowRandomVisits: ").append(allowRandomVisits).append("\n");
        sb.append("}");

        return sb.toString();
    }
}