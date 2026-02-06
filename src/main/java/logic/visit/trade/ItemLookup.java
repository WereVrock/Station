package logic.visit.trade;

import main.Game;
import main.Item;

import java.util.*;

public class ItemLookup {

    private final Game game;

    public ItemLookup(Game game) {
        this.game = game;
    }

    public List<Item> resolve(List<String> refs) {
        List<Item> items = new ArrayList<>();
        for (String ref : refs) {
            Item item = find(ref);
            if (item != null) items.add(item);
        }
        return items;
    }

    private Item find(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }
}