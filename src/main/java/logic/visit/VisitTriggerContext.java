package logic.visit;

import content.logic.triggers.engine.TriggerContext;
import main.Game;
import main.GameCharacter;
import main.Visit;

public class VisitTriggerContext implements TriggerContext {

    private final Game game;
    private final GameCharacter character;
    private final Visit visit;

    public VisitTriggerContext(Game game,
                               GameCharacter character,
                               Visit visit) {
        this.game = game;
        this.character = character;
        this.visit = visit;
    }

    public Game getGame() {
        return game;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public Visit getVisit() {
        return visit;
    }
}
