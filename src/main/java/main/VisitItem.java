package main;

import java.io.Serializable;

public class VisitItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // item reference by name or id
    public String item;

    // optional rarity modifier:
    // null | coinflip | rare | ultra_rare
    public String rarity;
}
