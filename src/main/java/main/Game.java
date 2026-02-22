package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import content.GameContentLoader;
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

    public int visitsToday = 0;

    public Player player = new Player();

    public int day = GameConstants.DAY_START;
    public boolean waitingForBurnChoice = true;

    private FireStatus fireStatus =
            new FireStatus(FireStatus.StrengthEnum.WEAK, "clean");

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

    public void loadContent(String characterFile) throws IOException {
    GameContentLoader loader = new GameContentLoader();
    loader.loadAll(this, characterFile);
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