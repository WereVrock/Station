// ===== Player.java =====
package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    public int food = 5;
    public int fuel = 5;
    public int money = 20;

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

