package logic.visit.resolve;

import logic.FireKeyNormalizer;
import logic.visit.MatchResult;
import tag.Tag;
import tag.TagManager;

import java.util.*;
import tag.TagManager;

public class VisitMatcher {

    public MatchResult evaluate(String fireEffect,
                                
                                List<String> fireReq,
                                List<String> tagReq,
                                List<String> legacyFire,
                                List<String> legacyTags) {

        List<String> sourceFire = fireReq.isEmpty() ? legacyFire : fireReq;
        List<String> sourceTags = tagReq.isEmpty() ? legacyTags : tagReq;

        List<String> normalizedFire = normalizeFire(sourceFire);

        MatchResult result = new MatchResult();
        result.requiredFire = normalizedFire;
        result.requiredTags = new ArrayList<>(sourceTags);
        result.actualFire = fireEffect;
        result.actualTags = new ArrayList<>(TagManager.view());

        result.fireOk = fireMatches(normalizedFire, fireEffect);
        result.tagsOk = tagsMatch(sourceTags);
        result.success = result.fireOk && result.tagsOk;

        return result;
    }

    // ---------------- helpers ----------------

    private List<String> normalizeFire(List<String> fireReq) {
        List<String> out = new ArrayList<>();
        for (String f : fireReq) {
            out.add(FireKeyNormalizer.normalize(f));
        }
        return out;
    }

    private boolean fireMatches(List<String> required, String actual) {
        return required.isEmpty() || required.contains(actual);
    }

    private boolean tagsMatch(List<String> required) {

        if (required.isEmpty()) return true;

        Set<String> present = new HashSet<>();
        for (Tag tag : TagManager.view()) {
            present.add(tag.getName());
        }

        for (String req : required) {
            if (!present.contains(req)) return false;
        }

        return true;
    }
}