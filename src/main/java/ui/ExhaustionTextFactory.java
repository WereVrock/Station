package ui;

import logic.VisitResult;
import main.GameCharacter;

public final class ExhaustionTextFactory {

    private ExhaustionTextFactory() {
        // utility class
    }

    // ===== Public API =====

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

    // ===== Core Logic =====

    private static String buildExhaustionText(
            VisitResult visit,
            GameCharacter character,
            ExhaustionType type
    ) {
        return switch (type) {
            case SELL_FOOD -> genericSellFood(character);
            case SELL_FUEL -> genericSellFuel(character);
            case BUY_FOOD  -> genericBuyFood(character);
            case BUY_FUEL  -> genericBuyFuel(character);
        };
    }

    private static String genericSellFood(GameCharacter character) {
        return "They fold the empty sack and slide it aside. "
             + "\"That was all the food I brought.\"";
    }

    private static String genericSellFuel(GameCharacter character) {
        return "They tighten the cap on the last can and pull it back. "
             + "\"No more fuel after that.\"";
    }

    private static String genericBuyFood(GameCharacter character) {
        return "They give a small nod, satisfied. "
             + "\"That’ll keep me fed.\"";
    }

    private static String genericBuyFuel(GameCharacter character) {
        return "They glance at the fire, then back to you. "
             + "\"That should burn long enough.\"";
    }

    private enum ExhaustionType {
        SELL_FOOD,
        SELL_FUEL,
        BUY_FOOD,
        BUY_FUEL
    }

    // ===== Test Harness =====

    public static void main(String[] args) {
        VisitResult visit = null;
        GameCharacter character = null;

        System.out.println("=== ExhaustionTextFactory Test ===\n");

        System.out.println("[SELL FOOD]");
        System.out.println(sellFoodExhausted(visit, character));
        System.out.println();

        System.out.println("[SELL FUEL]");
        System.out.println(sellFuelExhausted(visit, character));
        System.out.println();

        System.out.println("[BUY FOOD]");
        System.out.println(buyFoodExhausted(visit, character));
        System.out.println();

        System.out.println("[BUY FUEL]");
        System.out.println(buyFuelExhausted(visit, character));
        System.out.println();

        System.out.println("=== End Test ===");
    }
}