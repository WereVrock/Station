package content.logic.triggers.engine;

public interface Condition {
    boolean isTrue(TriggerContext context);
}