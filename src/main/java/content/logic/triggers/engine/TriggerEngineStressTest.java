package content.logic.triggers.engine;

import java.util.HashSet;
import java.util.Set;

public class TriggerEngineStressTest {

    // -------------------- TEST CONTEXT --------------------

    private static class TestContext implements TriggerContext {

        Set<String> tags = new HashSet<>();
        int score = 0;

        boolean hasTag(String tag) {
            return tags.contains(tag);
        }

        void addTag(String tag) {
            tags.add(tag);
        }

        void addScore(int value) {
            score += value;
        }
    }

    // -------------------- CONDITIONS --------------------

    private static class HasTagCondition implements Condition {

        private final String tag;

        private HasTagCondition(String tag) {
            this.tag = tag;
        }

        @Override
        public boolean isTrue(TriggerContext context) {
            return ((TestContext) context).hasTag(tag);
        }

        public static Condition fromSpec(SpecNode node) {
            String tag = (String) node.args.get("tag");
            return new HasTagCondition(tag);
        }
    }

    private static class ScoreAtLeastCondition implements Condition {

        private final int min;

        private ScoreAtLeastCondition(int min) {
            this.min = min;
        }

        @Override
        public boolean isTrue(TriggerContext context) {
            return ((TestContext) context).score >= min;
        }

        public static Condition fromSpec(SpecNode node) {
            Number n = (Number) node.args.get("min");
            return new ScoreAtLeastCondition(n.intValue());
        }
    }

    // -------------------- EFFECTS --------------------

    private static class AddTagEffect implements Effect {

        private final String tag;

        private AddTagEffect(String tag) {
            this.tag = tag;
        }

        @Override
        public void apply(TriggerContext context) {
            ((TestContext) context).addTag(tag);
        }

        public static Effect fromSpec(SpecNode node) {
            String tag = (String) node.args.get("tag");
            return new AddTagEffect(tag);
        }
    }

    private static class AddScoreEffect implements Effect {

        private final int value;

        private AddScoreEffect(int value) {
            this.value = value;
        }

        @Override
        public void apply(TriggerContext context) {
            ((TestContext) context).addScore(value);
        }

        public static Effect fromSpec(SpecNode node) {
            Number n = (Number) node.args.get("value");
            return new AddScoreEffect(n.intValue());
        }
    }

    // -------------------- MAIN TEST --------------------

    public static void main(String[] args) {

        System.out.println("=== Trigger Engine Stress Test ===");

        // Register plugins
        ConditionRegistry.register("hasTag", HasTagCondition::fromSpec);
        ConditionRegistry.register("scoreAtLeast", ScoreAtLeastCondition::fromSpec);

        EffectRegistry.register("addTag", AddTagEffect::fromSpec);
        EffectRegistry.register("addScore", AddScoreEffect::fromSpec);

        // Build triggers
        TriggerEngine engine = new TriggerEngine();

        engine.addTrigger(build(
                cond("hasTag", "tag", "A"),
                eff("addScore", "value", 10)
        ));

        engine.addTrigger(build(
                cond("scoreAtLeast", "min", 10),
                eff("addTag", "tag", "B")
        ));

        engine.addTrigger(build(
                cond("hasTag", "tag", "B"),
                eff("addScore", "value", 50)
        ));

        engine.addTrigger(build(
                cond("scoreAtLeast", "min", 60),
                eff("addTag", "tag", "WIN")
        ));

        // Run
        TestContext ctx = new TestContext();
        ctx.addTag("A");

        for (int i = 1; i <= 4; i++) {
            System.out.println("\n-- Tick " + i + " --");
            engine.evaluate(ctx);
            System.out.println("Tags: " + ctx.tags);
            System.out.println("Score: " + ctx.score);
        }

        System.out.println("\n=== FINAL STATE ===");
        System.out.println("Tags: " + ctx.tags);
        System.out.println("Score: " + ctx.score);
    }

    // -------------------- HELPERS --------------------

    private static Trigger build(SpecNode c, SpecNode e) {
        return new Trigger(
                ConditionFactory.create(c),
                EffectFactory.create(e)
        );
    }

    private static SpecNode cond(String type, String key, Object value) {
        SpecNode n = new SpecNode();
        n.type = type;
        n.args = new java.util.HashMap<>();
        n.args.put(key, value);
        return n;
    }

    private static SpecNode eff(String type, String key, Object value) {
        SpecNode n = new SpecNode();
        n.type = type;
        n.args = new java.util.HashMap<>();
        n.args.put(key, value);
        return n;
    }
}
