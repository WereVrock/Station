package content;

import main.Game;
import main.GameCharacter;
import tag.TagManager;
import content.logic.triggers.TriggerGameBindings;

import java.io.IOException;
import java.util.List;

public class GameContentLoader {

    private final CharacterContentLoader characterLoader;
    private final TagContentLoader tagLoader;

    private static boolean wired = false;

    public GameContentLoader() {
        this.characterLoader = new CharacterContentLoader();
        this.tagLoader = new TagContentLoader();
    }

    private static synchronized void wireTriggers() {
        if (wired) return;
        wired = true;
        TriggerGameBindings.install();
    }

    public void loadAll(Game game,
                        String characterFile,
                        String tagFile) throws IOException {

        // ensure trigger engine has all game bindings
        wireTriggers();

        // 1. Load tags first (world state)
        tagLoader.load(tagFile);

        // 2. Load characters (includes visits)
        List<GameCharacter> characters =
                characterLoader.load(characterFile);

        game.characters.clear();
        game.characters.addAll(characters);
    }
}
