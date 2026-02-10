package content.logic.triggers.engine;

import java.util.Map;

public class SpecNode {

    public String type;
    public Map<String, Object> args;

    @Override
    public String toString() {
        return "SpecNode{type='" + type + "', args=" + args + "}";
    }
}
