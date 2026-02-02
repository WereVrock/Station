package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Visit {

    // visit type: scripted / scheduled / random
    public String type;

    // conditions
    public List<String> fireRequired = new ArrayList<>();
    public List<String> requiredTags = new ArrayList<>();

    // dialogue lines
    public List<String> dialogue = new ArrayList<>();

    // world effects
    public List<String> tagsToAdd = new ArrayList<>();

    // state
    public boolean used = false;

    // --- SCRIPTED / SCHEDULED TIMING ---

    // JSON-facing fields (correct names)
    public Integer minDay;
    public Integer maxDay;

    // legacy/internal fields (kept for save compatibility)
    public Integer minDays;
    public Integer maxDays;

    public Integer firstEligibleDay = null;
    public Integer triggerDay = null;

    // optional per-character permissions
    public Boolean allowScriptedVisits;
    public Boolean allowScheduledVisits;
    public Boolean allowRandomVisits;

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
}
