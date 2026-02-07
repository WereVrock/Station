package logic.visit;

import tag.Tag;
import java.util.*;

public class MatchResult {

    // --- Required conditions ---
    public List<String> requiredFire = Collections.emptyList();
    public List<String> requiredTags = Collections.emptyList();

    // --- Actual world state ---
    public String actualFire;
    public List<Tag> actualTags = Collections.emptyList();

    // --- Evaluation flags ---
    public boolean fireOk;
    public boolean tagsOk;
    public boolean success;

    /**
     * Convenience view for logs / UI.
     * Do NOT use for logic.
     */
    public Set<String> actualTagNames() {
        Set<String> names = new HashSet<>();
        for (Tag tag : actualTags) {
            names.add(tag.getName());
        }
        return names;
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "fireOk=" + fireOk +
                ", tagsOk=" + tagsOk +
                ", success=" + success +
                ", requiredFire=" + requiredFire +
                ", requiredTags=" + requiredTags +
                ", actualFire='" + actualFire + '\'' +
                ", actualTags=" + actualTagNames() +
                '}';
    }
}
