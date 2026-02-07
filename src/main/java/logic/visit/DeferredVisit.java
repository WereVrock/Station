package logic.visit;

import main.Game;
import main.*;
import java.io.Serializable;

public class DeferredVisit implements Serializable {

    public final GameCharacter character;
    public final Visit visit;
    public final String fireEffect;
    public final int deferredFromDay;

    public DeferredVisit(GameCharacter character,
                         Visit visit,
                         String fireEffect,
                         int deferredFromDay) {

        this.character = character;
        this.visit = visit;
        this.fireEffect = fireEffect;
        this.deferredFromDay = deferredFromDay;
    }

    public boolean stillValid(Game game, VisitResolver resolver) {

        MatchResult match = resolver.evaluateDeferred(fireEffect,
                
                visit.visitFireRequired,
                visit.visitRequiredTags,
                visit.fireRequired,
                visit.requiredTags
        );

        return match.success && !character.visitedToday;
    }
}