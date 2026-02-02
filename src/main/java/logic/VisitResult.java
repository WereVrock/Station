package logic;

import main.GameCharacter;
import main.Item;

import java.util.ArrayList;
import java.util.List;

public class VisitResult {

    public final GameCharacter character;
    public final List<Item> itemsForSale;
    public final List<String> dialogue;
    public final String fireEffect;
    public final String visitType;

    public VisitResult(GameCharacter character, List<Item> itemsForSale, List<String> dialogue, String fireEffect, String visitType) {
        this.character = character;
        this.itemsForSale = itemsForSale != null ? itemsForSale : new ArrayList<>();
        this.dialogue = dialogue != null ? dialogue : new ArrayList<>();
        this.fireEffect = fireEffect != null ? fireEffect : "";
        this.visitType = visitType != null ? visitType : "unknown";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VisitResult {\n");
        sb.append("  character: ").append(character != null ? character : "null").append(",\n");
        sb.append("  visitType: '").append(visitType).append("',\n");
        sb.append("  itemsForSale: [");
        for (int i = 0; i < itemsForSale.size(); i++) {
            sb.append(itemsForSale.get(i));
            if (i < itemsForSale.size() - 1) sb.append(", ");
        }
        sb.append("],\n");
        sb.append("  dialogue: ").append(dialogue).append(",\n");
        sb.append("  fireEffect: '").append(fireEffect).append("'\n");
        sb.append("}");
        return sb.toString();
    }
}
