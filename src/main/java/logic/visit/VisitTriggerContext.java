package logic.visit;

import content.logic.triggers.engine.TriggerContext;
import main.Game;
import main.GameCharacter;
import main.Visit;

public class VisitTriggerContext implements TriggerContext {

 
    private final Visit visit;

    public VisitTriggerContext(Visit visit) {
     
        this.visit = visit;
    }

   

    public GameCharacter getCharacter() {
        return visit.character;
    }

    public Visit getVisit() {
        return visit;
    }
}
