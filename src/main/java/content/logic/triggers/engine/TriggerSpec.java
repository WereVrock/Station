package content.logic.triggers.engine;

public class TriggerSpec {

    public SpecNode condition;
    public SpecNode effect;

    @Override
    public String toString() {
        return "TriggerSpec{condition=" + condition + ", effect=" + effect + "}";
    }
}
