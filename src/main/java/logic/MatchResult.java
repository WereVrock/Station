package logic;

import java.util.List;
import java.util.Set;

public class MatchResult {
    public boolean success;
    public boolean fireOk;
    public boolean tagsOk;

    public List<String> requiredFire;
    public List<String> requiredTags;

    public String actualFire;
    public Set<String> actualTags;
}