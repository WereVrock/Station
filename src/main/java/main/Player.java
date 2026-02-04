package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public int food = 5;
    public int fuel = 5;
    public int money = 20;

    public List<ItemStack> inventory = new ArrayList<>();

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
        if (stack.isEmpty()) {
            inventory.remove(stack);
        }
    }
}