package logic;

import main.GameCharacter;
import main.Item;

import java.util.ArrayList;
import java.util.List;

public class VisitResult {

    public GameCharacter character;
    public List<Item> itemsForSale = new ArrayList<>();
    public List<String> dialogue = new ArrayList<>();
    public String fireEffect;
    public String type;

    // Constructor for easy creation in BurnResolver or other places
    public VisitResult(GameCharacter character, List<Item> itemsForSale,
                       List<String> dialogue, String fireEffect, String type) {
        this.character = character;
        this.itemsForSale = itemsForSale != null ? itemsForSale : new ArrayList<>();
        this.dialogue = dialogue != null ? dialogue : new ArrayList<>();
        this.fireEffect = fireEffect;
        this.type = type;
    }

    // Default no-arg constructor (optional, useful for JSON or other code)
    public VisitResult() {}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VisitResult {\n");
        sb.append("  character: ").append(character != null ? character.name : "null").append(",\n");
        sb.append("  itemsForSale: ").append(itemsForSale).append(",\n");
        sb.append("  dialogue: ").append(dialogue).append(",\n");
        sb.append("  fireEffect: ").append(fireEffect).append(",\n");
        sb.append("  type: ").append(type).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
