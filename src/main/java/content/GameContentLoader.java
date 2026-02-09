package content;

import main.Game;
import main.GameCharacter;
import tag.TagManager;

import java.io.IOException;
import java.util.List;

public class GameContentLoader {

    private final CharacterContentLoader characterLoader;
    private final TagContentLoader tagLoader;

    public GameContentLoader() {
        this.characterLoader = new CharacterContentLoader();
        this.tagLoader = new TagContentLoader();
    }

    public void loadAll(Game game,
                        String characterFile,
                        String tagFile) throws IOException {

        // 1. Load tags first (world state)
        tagLoader.load(tagFile);

        // 2. Load characters (includes visits)
        List<GameCharacter> characters =
                characterLoader.load(characterFile);

        game.characters.clear();
        game.characters.addAll(characters);
    }
}
