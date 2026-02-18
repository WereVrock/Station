package ui;

import main.VisitResult;
import main.GameCharacter;
import main.Visit;

public final class ExhaustionTextFactory {

    private ExhaustionTextFactory() {
        // utility class
    }

    // ===== Public API (USED BY OUTSIDE) =====

    public static String sellFoodExhausted(VisitResult visit, GameCharacter character) {
        return buildExhaustionText(visit, character, ExhaustionType.SELL_FOOD);
    }

    public static String sellFuelExhausted(VisitResult visit, GameCharacter character) {
        return buildExhaustionText(visit, character, ExhaustionType.SELL_FUEL);
    }

    public static String buyFoodExhausted(VisitResult visit, GameCharacter character) {
        return buildExhaustionText(visit, character, ExhaustionType.BUY_FOOD);
    }

    public static String buyFuelExhausted(VisitResult visit, GameCharacter character) {
        return buildExhaustionText(visit, character, ExhaustionType.BUY_FUEL);
    }

    // ===== Core Resolution =====

    static String buildExhaustionText(
            VisitResult visitResult,
            GameCharacter character,
            ExhaustionType type
    ) {
        Visit visit = visitResult != null ? visitResult.character != null
                ? findVisit(visitResult, character)
                : null
                : null;

        // 1. Visit-specific text
        if (visit != null) {
            String text = visit.getExhaustionText(type);
            if (text != null) return text;
        }

        // 2. Character-specific text
        if (character != null) {
            String text = character.getExhaustionText(type);
            if (text != null) return text;
        }

        // 3. Generic fallback
        return switch (type) {
            case SELL_FOOD -> genericSellFood(character);
            case SELL_FUEL -> genericSellFuel(character);
            case BUY_FOOD  -> genericBuyFood(character);
            case BUY_FUEL  -> genericBuyFuel(character);
        };
    }

    // ===== Generic Text =====

    static String genericSellFood(GameCharacter character) {
        return "They fold the empty sack and slide it aside. "
             + "\"That was all the food I brought.\"";
    }

    static String genericSellFuel(GameCharacter character) {
        return "They tighten the cap on the last can and pull it back. "
             + "\"No more fuel after that.\"";
    }

    static String genericBuyFood(GameCharacter character) {
        return "They give a small nod, satisfied. "
             + "\"That’ll keep me fed.\"";
    }

    static String genericBuyFuel(GameCharacter character) {
        return "They glance at the fire, then back to you. "
             + "\"That should burn long enough.\"";
    }

    // ===== Helpers =====

    private static Visit findVisit(VisitResult visitResult, GameCharacter character) {
        if (character == null) return null;

        for (Visit v : character.visits) {
            if (v.dialogue == visitResult.dialogue) {
                return v;
            }
        }
        return null;
    }

    // ===== Types =====

    public enum ExhaustionType {
        SELL_FOOD,
        SELL_FUEL,
        BUY_FOOD,
        BUY_FUEL
    }

    // ===== Test Harness (KEPT) =====

    public static void main(String[] args) {
        System.out.println("=== ExhaustionTextFactory Loaded ===");
    }
}