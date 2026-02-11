package content.logic.triggers;

import content.logic.triggers.engine.SpecNode;

public final class TriggerSpec {

    public String event;      // e.g. "VISIT_START"
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