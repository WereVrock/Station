package content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tag.Tag;
import tag.TagManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TagContentLoader {

    private final Gson gson = new Gson();

    public void load(String file) throws IOException {

        if (file == null) return;

        Type listType =
                new TypeToken<ArrayList<TagSpec>>() {}.getType();

        InputStream is =
                getClass().getClassLoader().getResourceAsStream(file);

        if (is == null) {
            throw new IOException("Tag JSON not found: " + file);
        }

        try (InputStreamReader reader = new InputStreamReader(is)) {

            List<TagSpec> specs = gson.fromJson(reader, listType);

            if (specs == null) return;

            for (TagSpec spec : specs) {
                TagManager.add(spec.toTag());
            }
        }
    }
}