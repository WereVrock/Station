package logic;

import main.GameCharacter;
import main.Item;

import java.util.ArrayList;
import java.util.List;

public class VisitResult {

    public GameCharacter character;
    public List<Item> itemsForSale = new ArrayList<>();
    public List<Item> itemsWanted = new ArrayList<>();
    public List<String> dialogue = new ArrayList<>();
    public String fireEffect;
    public String type;

    // NEW full constructor
    public VisitResult(GameCharacter character,
                       List<Item> itemsForSale,
                       List<Item> itemsWanted,
                       List<String> dialogue,
                       String fireEffect,
                       String type) {

        this.character = character;
        this.itemsForSale = itemsForSale != null ? itemsForSale : new ArrayList<>();
        this.itemsWanted = itemsWanted != null ? itemsWanted : new ArrayList<>();
        this.dialogue = dialogue != null ? dialogue : new ArrayList<>();
        this.fireEffect = fireEffect;
        this.type = type;
    }

    // BACKWARD-COMPAT constructor (intentional)
    public VisitResult(GameCharacter character,
                       List<Item> itemsForSale,
                       List<String> dialogue,
                       String fireEffect,
                       String type) {

        this(character, itemsForSale, new ArrayList<>(), dialogue, fireEffect, type);
    }

    public VisitResult() {}

    public boolean wants(Item item) {
        return itemsWanted.contains(item);
    }

    @Override
    public String toString() {
        return "VisitResult {\n" +
                "  character=" + (character != null ? character.name : "null") + ",\n" +
                "  itemsForSale=" + itemsForSale + ",\n" +
                "  itemsWanted=" + itemsWanted + ",\n" +
                "  dialogue=" + dialogue + ",\n" +
                "  fireEffect=" + fireEffect + ",\n" +
                "  type=" + type + "\n" +
                "}";
    }
}
