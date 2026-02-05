package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public String id;
    public String name;
    public String type;

    public int basePrice = GameConstants.DEFAULT_ITEM_BASE_PRICE;

    // NEW: split fire data
    public FireStatus.Strength fireStrength = FireStatus.Strength.WEAK;
    public String fireEffect = "clean";

    public boolean burnable = true;

    public List<String> tags = new ArrayList<>();

    @Override
    public String toString() {
        return "Item {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", basePrice=" + basePrice +
                ", fireStrength=" + fireStrength +
                ", fireEffect='" + fireEffect + '\'' +
                ", burnable=" + burnable +
                ", tags=" + tags +
                '}';
    }
}