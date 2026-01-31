package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;
    public String type;
    public String fireEffect;
    public boolean burnable = true;

    public List<String> tags = new ArrayList<>();
}
