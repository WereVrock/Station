package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    // immutable identity
    public String id;

    // player-visible data
    public String name;
    public String type;

    // base price for trade
    public int basePrice = 1;

    // fire interaction
    public String fireEffect;
    public boolean burnable = true;

    // tags added to world on burn
    public List<String> tags = new ArrayList<>();

    @Override
    public String toString() {
        return "Item {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", basePrice=" + basePrice +
                ", fireEffect='" + fireEffect + '\'' +
                ", burnable=" + burnable +
                ", tags=" + tags +
                '}';
    }
}
