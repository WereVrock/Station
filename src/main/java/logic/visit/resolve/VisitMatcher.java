package logic.visit.resolve;

import logic.FireKeyNormalizer;
import logic.visit.MatchResult;

import java.util.*;
import logic.visit.MatchResult;

public class VisitMatcher {

    public MatchResult evaluate(String fireEffect,
                                Set<String> worldTags,
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
        result.actualTags = new HashSet<>(worldTags);

        result.fireOk = normalizedFire.isEmpty() || normalizedFire.contains(fireEffect);
        result.tagsOk = worldTags.containsAll(sourceTags);
        result.success = result.fireOk && result.tagsOk;

        return result;
    }
}