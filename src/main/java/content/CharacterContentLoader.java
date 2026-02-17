package content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import content.logic.triggers.engine.Trigger;
import content.logic.triggers.engine.TriggerCompiler;
import content.logic.triggers.engine.TriggerSpec;
import main.GameCharacter;
import main.Visit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CharacterContentLoader {

    private final Gson gson = new Gson();

    public List<GameCharacter> load(String file) throws IOException {

        Type listType =
                new TypeToken<ArrayList<GameCharacter>>() {}.getType();

        InputStream is =
                getClass().getClassLoader().getResourceAsStream(file);

        if (is == null) {
            throw new IOException("Character JSON not found: " + file);
        }

        List<GameCharacter> characters;

        try (InputStreamReader reader = new InputStreamReader(is)) {
            characters = gson.fromJson(reader, listType);
        }

        if (characters == null) {
            return new ArrayList<>();
        }

        // ---- WIRE VISIT -> CHARACTER REFERENCES ----
        for (GameCharacter c : characters) {
            if (c.visits == null) continue;

            for (Visit v : c.visits) {
                v.character = c;
            }
        }

        // ---- COMPILE TRIGGERS ----
        for (GameCharacter c : characters) {

            if (c.visits == null) continue;

            for (Visit v : c.visits) {

                if (v.triggers == null || v.triggers.isEmpty()) continue;

                List<Trigger> runtime = new ArrayList<>();

                for (TriggerSpec spec : v.triggers) {
                    try {
                        Trigger compiled = TriggerCompiler.compile(spec);
                        runtime.add(compiled);
                    } catch (Exception e) {
                        System.out.println("Trigger compile failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                v.runtimeTriggers = runtime;
            }
        }

        return characters;
    }
}