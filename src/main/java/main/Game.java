package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Item> items = new ArrayList<>();
    public List<GameCharacter> characters = new ArrayList<>();
    public Set<String> worldTags = new HashSet<>();

    // NEW: per-day visit tracking
    public Set<String> charactersVisitedToday = new HashSet<>();

    public Player player = new Player();

    public int day = 1;
    public boolean waitingForBurnChoice = true;

    private transient Scanner scanner = new Scanner(System.in);

    // ---------- JSON CONTENT LOAD ----------
    public void loadItems(String file) throws IOException {
        Gson gson = new Gson();
        Type itemListType = new TypeToken<ArrayList<Item>>() {}.getType();

        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        if (is == null) throw new IOException("Resource not found: " + file);

        try (InputStreamReader reader = new InputStreamReader(is)) {
            items = gson.fromJson(reader, itemListType);
        }
    }

    public void loadCharacters(String file) throws IOException {
        Gson gson = new Gson();
        Type charListType = new TypeToken<ArrayList<GameCharacter>>() {}.getType();

        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        if (is == null) throw new IOException("Resource not found: " + file);

        try (InputStreamReader reader = new InputStreamReader(is)) {
            characters = gson.fromJson(reader, charListType);
        }
    }

    // ---------- DAY CONTROL ----------
    public void startNewDay() {
        waitingForBurnChoice = true;
        charactersVisitedToday.clear();
        day++;
    }

    public void burnChosen() {
        waitingForBurnChoice = false;
    }

    // ---------- SAVE ----------
    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    // ---------- LOAD ----------
    public static Game loadGame(String filename)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filename))) {

            Game game = (Game) in.readObject();
            game.scanner = new Scanner(System.in);
            return game;
        }
    }
}
