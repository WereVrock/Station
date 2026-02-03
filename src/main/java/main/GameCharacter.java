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

    // character inventory (visit-scoped)
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

    public void clearInventory() {
        inventory.clear();
    }

    public javax.swing.ImageIcon getPortraitIcon() {
        String path = "/images/character_placeholder.png";

        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            return null;
        }

        return new javax.swing.ImageIcon(url);
    }

    // ===== TO STRING =====
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameCharacter {\n");

        sb.append("  id: ").append(id).append(",\n");

        sb.append("  allowScriptedVisits: ").append(allowScriptedVisits).append(",\n");
        sb.append("  allowScheduledVisits: ").append(allowScheduledVisits).append(",\n");
        sb.append("  allowRandomVisits: ").append(allowRandomVisits).append(",\n");

        sb.append("  inventory: [");
        for (int i = 0; i < inventory.size(); i++) {
            sb.append(inventory.get(i));
            if (i < inventory.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]\n");

        sb.append("}");
        return sb.toString();
    }
}
