package logic.visit.resolve;

import main.GameCharacter;
import main.Visit;

public class VisitEligibility {

    public boolean isAllowed(GameCharacter character, Visit visit) {
        switch (visit.type) {
            case "scripted": return character.allowScriptedVisits;
            case "scheduled": return character.allowScheduledVisits;
            case "random": return character.allowRandomVisits;
            default: return true;
        }
    }
}