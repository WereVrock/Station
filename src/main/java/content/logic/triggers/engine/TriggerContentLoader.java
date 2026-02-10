package content.logic.triggers.engine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TriggerContentLoader {

    private final Gson gson = new Gson();

    public List<Trigger> load(String file) throws IOException {

        Type listType =
                new TypeToken<ArrayList<TriggerSpec>>() {}.getType();

        InputStream is =
                getClass().getClassLoader().getResourceAsStream(file);

        if (is == null) {
            throw new IOException("Trigger JSON not found: " + file);
        }

        try (InputStreamReader reader = new InputStreamReader(is)) {

            List<TriggerSpec> specs = gson.fromJson(reader, listType);
            List<Trigger> triggers = new ArrayList<>();

            if (specs == null) return triggers;

            for (TriggerSpec spec : specs) {
                Condition condition = ConditionFactory.create(spec.condition);
                Effect effect = EffectFactory.create(spec.effect);
                triggers.add(new Trigger(condition, effect));
            }

            return triggers;
        }
    }
}
