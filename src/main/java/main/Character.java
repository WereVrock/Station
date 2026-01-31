package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Character implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String background;
    public List<Visit> visits = new ArrayList<>();
}
