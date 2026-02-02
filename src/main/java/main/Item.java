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

    // fire interaction
    public String fireEffect;
    public boolean burnable = true;

    // tags added to world on burn
    public List<String> tags = new ArrayList<>();
}
