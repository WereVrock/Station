package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public int food = GameConstants.PLAYER_START_FOOD;
    public int fuel = GameConstants.PLAYER_START_FUEL;
    public int money = GameConstants.PLAYER_START_MONEY;

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
            inventory.add(new ItemStack(item, GameConstants.STACK_AMOUNT));
        } else {
            stack.increment(GameConstants.STACK_AMOUNT);
        }
    }

    public void removeItem(Item item) {
        ItemStack stack = getStack(item);
        if (stack == null) return;

        stack.decrement(GameConstants.STACK_AMOUNT);
        if (stack.isEmpty()) {
            inventory.remove(stack);
        }
    }
}