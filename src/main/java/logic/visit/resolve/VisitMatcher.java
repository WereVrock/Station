package logic.visit.resolve;

import logic.FireKeyNormalizer;
import logic.visit.MatchResult;
import tag.Tag;
import tag.TagManager;

import java.util.*;

public class VisitMatcher {

    public MatchResult evaluate(String fireEffect,
                                TagManager worldTags,
                                List<String> fireReq,
                                List<String> tagReq,
                                List<String> legacyFire,
                                List<String> legacyTags) {

        List<String> sourceFire = fireReq.isEmpty() ? legacyFire : fireReq;
        List<String> sourceTags = tagReq.isEmpty() ? legacyTags : tagReq;

        List<String> normalizedFire = new ArrayList<>();
        for (String fire : sourceFire) {
            normalizedFire.add(FireKeyNormalizer.normalize(fire));
        }

        MatchResult result = new MatchResult();
        result.requiredFire = new ArrayList<>(normalizedFire);
        result.requiredTags = new ArrayList<>(sourceTags);
        result.actualFire = fireEffect;
        result.actualTags = collectTagNames(worldTags);

        result.fireOk = normalizedFire.isEmpty() || normalizedFire.contains(fireEffect);
        result.tagsOk = hasAllTags(worldTags, sourceTags);
        result.success = result.fireOk && result.tagsOk;

        return result;
    }

    private boolean hasAllTags(TagManager manager, List<String> required) {

        if (required.isEmpty()) return true;

        Set<String> present = collectTagNames(manager);

        for (String req : required) {
            if (!present.contains(req)) return false;
        }

        return true;
    }

    private Set<String> collectTagNames(TagManager manager) {

        Set<String> names = new HashSet<>();

        for (Tag tag : manager.view()) {
            names.add(tag.getName());
        }

        return names;
    }
}