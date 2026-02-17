// ===== GameCharacter.java =====
// ===== GameCharacter.java =====
package main;

import ui.ExhaustionTextFactory.ExhaustionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    // immutable identity
    public String id;

    // player-visible data
    public String name;
    public String background;

    // visit permissions
    public boolean allowScriptedVisits = true;
    public boolean allowScheduledVisits = true;
    public boolean allowRandomVisits = true;

    
    public List<Visit> visits = new ArrayList<Visit>() ;

    // runtime state
    public boolean visitedToday = false;

    // stacked inventory
    public List<ItemStack> inventory = new ArrayList<>();

    // exhaustion text
    private final Map<ExhaustionType, String> exhaustionText =
            new EnumMap<>(ExhaustionType.class);

    // ===== EXHAUSTION TEXT =====
    public void setExhaustionText(ExhaustionType type, String text) {
        if (text == null) {
            exhaustionText.remove(type);
        } else {
            exhaustionText.put(type, text);
        }
    }

    public String getExhaustionText(ExhaustionType type) {
        return exhaustionText.get(type);
    }

    // ===== INVENTORY HELPERS =====
    public boolean hasItem(Item item) {
        return getStack(item) != null;
    }

    public ItemStack getStack(Item item) {
        for (ItemStack s : inventory) {
            if (s.item == item) return s;
        }
        return null;
    }

    public void addItem(Item item) {
        ItemStack stack = getStack(item);
        if (stack == null) {
            inventory.add(new ItemStack(item, 1));
        } else {
            stack.increment(1);
        }
    }

    public void removeItem(Item item) {
        ItemStack stack = getStack(item);
        if (stack == null) return;

        stack.decrement(1);
        if (stack.isEmpty()) inventory.remove(stack);
    }

    public void clearInventory() {
        inventory.clear();
    }

    public javax.swing.ImageIcon getPortraitIcon() {
        String path = "/images/character_placeholder.png";
        java.net.URL url = getClass().getResource(path);
        return url != null ? new javax.swing.ImageIcon(url) : null;
    }

    // ===== TO STRING =====
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameCharacter {\n");
        sb.append("  id: ").append(id).append(",\n");
        sb.append("  name: ").append(name).append(",\n");

        sb.append("  allowScriptedVisits: ").append(allowScriptedVisits).append(",\n");
        sb.append("  allowScheduledVisits: ").append(allowScheduledVisits).append(",\n");
        sb.append("  allowRandomVisits: ").append(allowRandomVisits).append(",\n");

        sb.append("  visits: ").append(visits.size()).append(",\n");

        sb.append("  inventory: [");
        for (int i = 0; i < inventory.size(); i++) {
            sb.append(inventory.get(i));
            if (i < inventory.size() - 1) sb.append(", ");
        }
        sb.append("]\n");

        sb.append("}");
        return sb.toString();
    }
}

