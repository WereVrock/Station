package content.logic.triggers.engine;

import java.util.*;

public class TriggerEngineTest {

    enum TestEvent {
        TICK,
        BATTLE,
        VISIT
    }

    static class TestContext implements TriggerContext {
        private final Set<String> tags = new HashSet<>();
        private int score = 0;

        boolean hasTag(String tag) {
            return tags.contains(tag);
        }

        void addTag(String tag) {
            tags.add(tag);
        }

        void addScore(int v) {
            score += v;
        }

        @Override
        public String toString() {
            return "Tags=" + tags + " Score=" + score;
        }
    }

    static class HasTagCondition implements Condition {

        private final String tag;

        HasTagCondition(String tag) {
            this.tag = tag;
        }

        @Override
        public boolean isTrue(TriggerContext context) {
            return ((TestContext) context).hasTag(tag);
        }

        static Condition fromSpec(SpecNode node) {
            return new HasTagCondition((String) node.args.get("tag"));
        }
    }

    static class ScoreAtLeastCondition implements Condition {

        private final int min;

        ScoreAtLeastCondition(int min) {
            this.min = min;
        }

        @Override
        public boolean isTrue(TriggerContext context) {
            return ((TestContext) context).score >= min;
        }

        static Condition fromSpec(SpecNode node) {
            return new ScoreAtLeastCondition(
                    ((Number) node.args.get("min")).intValue());
        }
    }

    static class AddTagEffect implements Effect {

        private final String tag;

        AddTagEffect(String tag) {
            this.tag = tag;
        }

        @Override
        public void apply(TriggerContext context) {
            ((TestContext) context).addTag(tag);
        }

        static Effect fromSpec(SpecNode node) {
            return new AddTagEffect((String) node.args.get("tag"));
        }
    }

    static class AddScoreEffect implements Effect {

        private final int value;

        AddScoreEffect(int value) {
            this.value = value;
        }

        @Override
        public void apply(TriggerContext context) {
            ((TestContext) context).addScore(value);
        }

        static Effect fromSpec(SpecNode node) {
            return new AddScoreEffect(
                    ((Number) node.args.get("value")).intValue());
        }
    }

    public static void main(String[] args) {

        ConditionRegistry.register("hasTag", HasTagCondition::fromSpec);
        ConditionRegistry.register("scoreAtLeast", ScoreAtLeastCondition::fromSpec);

        EffectRegistry.register("addTag", AddTagEffect::fromSpec);
        EffectRegistry.register("addScore", AddScoreEffect::fromSpec);

        List<Trigger> triggers = List.of(
                build(
                        "TICK",
                        cond("hasTag", "tag", "A"),
                        eff("addScore", "value", 10)
                ),
                build(
                        "TICK",
                        cond("scoreAtLeast", "min", 10),
                        eff("addTag", "tag", "B")
                ),
                build(
                        "TICK",
                        cond("hasTag", "tag", "B"),
                        eff("addScore", "value", 50)
                ),
                build(
                        "TICK",
                        cond("scoreAtLeast", "min", 60),
                        eff("addTag", "tag", "WIN")
                )
        );

        TestContext ctx = new TestContext();
        ctx.addTag("A");

        System.out.println("=== Trigger Engine Test ===");

        for (int i = 1; i <= 3; i++) {
            System.out.println("\nTick " + i);
            TriggerEngine.evaluate(triggers, "TICK", ctx);
            System.out.println(ctx);
        }
    }

    private static Trigger build(String event,
                                 SpecNode c,
                                 SpecNode e) {

        return new Trigger(
                event,
                ConditionFactory.create(c),
                EffectFactory.create(e)
        );
    }

    private static SpecNode cond(String type, String key, Object value) {
        SpecNode n = new SpecNode();
        n.type = type;
        n.args = new HashMap<>();
        n.args.put(key, value);
        return n;
    }

    private static SpecNode eff(String type, String key, Object value) {
        SpecNode n = new SpecNode();
        n.type = type;
        n.args = new HashMap<>();
        n.args.put(key, value);
        return n;
    }
}