package content;

import main.Game;
import main.GameCharacter;
import content.logic.triggers.TriggerGameBindings;

import java.io.IOException;
import java.util.List;

public class GameContentLoader {

    private final CharacterContentLoader characterLoader;

    private static boolean wired = false;

    public GameContentLoader() {
        this.characterLoader = new CharacterContentLoader();
    }

    private static synchronized void wireTriggers() {
        if (wired) return;
        wired = true;
        TriggerGameBindings.install();
    }

    public void loadAll(Game game,
                        String characterFile) throws IOException {

        // Ensure trigger engine bindings installed
        wireTriggers();

        // Load characters (includes visit trigger compilation)
        List<GameCharacter> characters =
                characterLoader.load(characterFile);

        game.characters.clear();
        game.characters.addAll(characters);
    }
}