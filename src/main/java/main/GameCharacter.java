package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    // immutable identity
    public String id;

    // player-visible data
    public String name;
    public String background;

    // visit permissions (global gates)
    public boolean allowScriptedVisits = true;
    public boolean allowScheduledVisits = true;
    public boolean allowRandomVisits = true;

    // visits defined in JSON
    public List<Visit> visits = new ArrayList<>();

    // runtime state
    public boolean visitedToday = false;

    // character inventory (used only during visits)
    public List<Item> inventory = new ArrayList<>();

    // ===== INVENTORY HELPERS =====

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
