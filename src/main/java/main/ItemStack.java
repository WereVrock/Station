package main;

import java.io.Serializable;

public class ItemStack implements Serializable {

    private static final long serialVersionUID = 1L;

    public Item item;
    public int count;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public void increment(int amount) {
        count += amount;
    }

    public void decrement(int amount) {
        count -= amount;
    }

    public boolean isEmpty() {
        return count <= 0;
    }

    @Override
    public String toString() {
        return item.name + " x" + count;
    }
}