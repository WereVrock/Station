package logic.visit.resolve;

import main.Game;
import main.GameCharacter;
import main.Visit;

import java.util.*;

public class VisitSelector {

    public List<GameCharacter> shuffledCharacters(Game game, Random rng) {
        List<GameCharacter> list = new ArrayList<>(game.characters);
        Collections.shuffle(list, rng);
        return list;
    }

    public List<Visit> randomVisits(GameCharacter character) {
        List<Visit> result = new ArrayList<>();
        for (Visit v : character.visits) {
            if ("random".equals(v.type)) {
                result.add(v);
            }
        }
        Collections.shuffle(result);
        return result;
    }
}