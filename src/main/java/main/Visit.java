package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Visit implements Serializable {

    private static final long serialVersionUID = 1L;

    // one of: scripted | scheduled | random
    public String type;

    // appearance conditions
    public List<String> fireRequired = new ArrayList<>();
    public List<String> requiredTags = new ArrayList<>();

    // timing (used by scripted / scheduled)
    public int minDay = 0;
    public int maxDay = 0;

    // state
    public boolean used = false;

    // dialogue lines (order preserved)
    public List<String> dialogue = new ArrayList<>();

    // trading
    public List<VisitItem> sells = new ArrayList<>();
    public List<VisitItem> buys = new ArrayList<>();

    // money delta applied on visit trigger
    public int goldChange = 0;

    // portrait override
    public String portraitOverride;
    public boolean portraitOneTime = false;

    // NEW: world tags added when this visit triggers
    public List<String> tagsToAdd = new ArrayList<>();

    // permission toggles
    public Boolean allowScriptedVisits;
    public Boolean allowScheduledVisits;
    public Boolean allowRandomVisits;
}
