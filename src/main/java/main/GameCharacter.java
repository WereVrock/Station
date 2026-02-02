package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    // immutable identity
    public String id;

    // player-visible
    public String name;
    public String background;
    public String defaultPortrait;

    // economy
    public int gold = 0;

    // visit permission gates
    public boolean scriptedVisitsAllowed = true;
    public boolean scheduledVisitsAllowed = true;
    public boolean randomVisitsAllowed = true;

    // visits defined in JSON
    public List<Visit> visits = new ArrayList<>();

    // runtime inventory (used only during visits)
    public List<Item> inventory = new ArrayList<>();

    // -------- inventory helpers --------

    public boolean hasItem(Item item) {
        return inventory.contains(item);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }
}
