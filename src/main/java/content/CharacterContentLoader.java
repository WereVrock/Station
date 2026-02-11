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

//        System.out.println("=== CharacterContentLoader.load START ===");
//        System.out.println("Loading file: " + file);

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
//            System.out.println("No characters loaded (JSON returned null).");
            return new ArrayList<>();
        }

//        System.out.println("Characters loaded: " + characters.size());

        // ---- Compile trigger specs into runtime triggers ----
        for (GameCharacter c : characters) {

//            System.out.println("\nCharacter: " + c.name);

            if (c.visits == null) {
//                System.out.println("  Visits: null");
                continue;
            }

//            System.out.println("  Visits count: " + c.visits.size());

            for (Visit v : c.visits) {

//                System.out.println("    Visit type: " + v.type);

                if (v.triggers == null) {
//                    System.out.println("      Triggers: null");
                    continue;
                }

                if (v.triggers.isEmpty()) {
//                    System.out.println("      Triggers: empty");
                    continue;
                }

//                System.out.println("      TriggerSpecs count: " + v.triggers.size());

                List<Trigger> runtime = new ArrayList<>();

                for (TriggerSpec spec : v.triggers) {

//                    System.out.println("        Compiling TriggerSpec:");
//                    System.out.println("          Event: " + spec.event);
//                    System.out.println("          Condition: " + spec.condition);
//                    System.out.println("          Effect: " + spec.effect);

                    try {
                        Trigger compiled = TriggerCompiler.compile(spec);
                        runtime.add(compiled);
                        System.out.println("          → Compiled OK");
                    } catch (Exception e) {
                        System.out.println("          → Compilation FAILED: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                v.runtimeTriggers = runtime;

//                System.out.println("      RuntimeTriggers assigned: " +
//                        (v.runtimeTriggers == null ? "null" : v.runtimeTriggers.size()));
            }
        }

//        System.out.println("\n=== CharacterContentLoader.load END ===");

        return characters;
    }
}
