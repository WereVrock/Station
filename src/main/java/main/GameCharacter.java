// ===== Character.java =====
package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameCharacter implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String background;
    public List<Visit> visits = new ArrayList<>();

    // NEW: character inventory
    public List<Item> inventory = new ArrayList<>();

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
