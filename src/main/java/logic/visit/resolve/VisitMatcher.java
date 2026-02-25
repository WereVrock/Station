package logic.visit.resolve;

import logic.FireStatusMatcher;
import logic.visit.MatchResult;
import tag.Tag;
import tag.TagManager;

import java.util.*;

public class VisitMatcher {

    public MatchResult evaluate(
            List<String> fireReq,
            List<String> tagReq) {

        

        MatchResult result = new MatchResult();
        result.requiredFire = new ArrayList<>(fireReq);
        result.requiredTags = new ArrayList<>(tagReq);
        result.actualTags = new ArrayList<>(TagManager.view());

        result.fireOk = fireMatches(fireReq);
        result.tagsOk = tagsMatch(tagReq);
        result.success = result.fireOk && result.tagsOk;

        return result;
    }

    private boolean fireMatches(List<String> required) {
        return required.isEmpty() || FireStatusMatcher.matchesAny(required);
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