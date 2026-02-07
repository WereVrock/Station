package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tag.TagManager;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import main.FireStatus;
import main.GameCharacter;
import main.GameConstants;
import main.Item;
import main.Player;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Item> items = new ArrayList<>();
    public List<GameCharacter> characters = new ArrayList<>();

    public TagManager worldTags = new TagManager();

    public int visitsToday = 0;

    public Player player = new Player();

    public int day = GameConstants.DAY_START;
    public boolean waitingForBurnChoice = true;

    private FireStatus fireStatus =
            new FireStatus(FireStatus.Strength.WEAK, "clean");

    private transient Scanner scanner = new Scanner(System.in);

    public FireStatus getFireStatus() {
        return fireStatus;
    }

    public void setFireStatus(FireStatus fireStatus) {
        this.fireStatus = fireStatus;
    }

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

    public void startNewDay() {
        waitingForBurnChoice = true;
        visitsToday = 0;
        day++;

        worldTags.onNewDay();

        for (GameCharacter c : characters) {
            c.visitedToday = false;
        }
    }

    public void burnChosen() {
        waitingForBurnChoice = false;
    }

    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

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