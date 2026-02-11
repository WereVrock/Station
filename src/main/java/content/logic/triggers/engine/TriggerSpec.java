package content.logic.triggers.engine;

public final class TriggerSpec {

    public String event;
    public SpecNode condition;
    public SpecNode effect;

    @Override
    public String toString() {
        return "TriggerSpec{" +
                "event='" + event + '\'' +
                ", condition=" + condition +
                ", effect=" + effect +
                '}';
    }
}