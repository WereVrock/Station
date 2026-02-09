package content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.GameCharacter;

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

        try (InputStreamReader reader = new InputStreamReader(is)) {
            return gson.fromJson(reader, listType);
        }
    }
}