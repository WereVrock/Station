package main;

import main.GameCharacter;
import main.Item;

import java.util.ArrayList;
import java.util.List;
import main.Visit;

public class VisitResult {

    public GameCharacter character;

    public final Visit visit;
    public List<Item> itemsForSale = new ArrayList<>();
    public List<Item> itemsWanted = new ArrayList<>();

    public int sellFood;
    public int sellFuel;
    public int buyFood;
    public int buyFuel;

    // NEW resolved prices
    public int sellFoodPrice;
    public int sellFuelPrice;
    public int buyFoodPrice;
    public int buyFuelPrice;

    public List<String> dialogue = new ArrayList<>();
    public String fireEffect;
    public String type;

    public VisitResult(GameCharacter character,
            List<Item> itemsForSale,
            List<Item> itemsWanted,
            List<String> dialogue,
            String fireEffect,
            String type,
            int sellFood,
            int sellFuel,
            int buyFood,
            int buyFuel,
            int sellFoodPrice,
            int sellFuelPrice,
            int buyFoodPrice,
            int buyFuelPrice,
            Visit visit) {
        this.visit = visit;
        this.character = character;
        this.itemsForSale = itemsForSale != null ? itemsForSale : new ArrayList<>();
        this.itemsWanted = itemsWanted != null ? itemsWanted : new ArrayList<>();
        this.dialogue = dialogue != null ? dialogue : new ArrayList<>();
        this.fireEffect = fireEffect;
        this.type = type;

        this.sellFood = sellFood;
        this.sellFuel = sellFuel;
        this.buyFood = buyFood;
        this.buyFuel = buyFuel;

        this.sellFoodPrice = sellFoodPrice;
        this.sellFuelPrice = sellFuelPrice;
        this.buyFoodPrice = buyFoodPrice;
        this.buyFuelPrice = buyFuelPrice;
    }

    public boolean wants(Item item) {
        return itemsWanted.contains(item);
    }

    @Override
    public String toString() {
        return "VisitResult {\n"
                + "  character=" + (character != null ? character.name : "null") + ",\n"
                + "  itemsForSale=" + itemsForSale + ",\n"
                + "  itemsWanted=" + itemsWanted + ",\n"
                + "  sellFood=" + sellFood + ",\n"
                + "  sellFuel=" + sellFuel + ",\n"
                + "  buyFood=" + buyFood + ",\n"
                + "  buyFuel=" + buyFuel + ",\n"
                + "  sellFoodPrice=" + sellFoodPrice + ",\n"
                + "  sellFuelPrice=" + sellFuelPrice + ",\n"
                + "  buyFoodPrice=" + buyFoodPrice + ",\n"
                + "  buyFuelPrice=" + buyFuelPrice + ",\n"
                + "  dialogue=" + dialogue + ",\n"
                + "  fireEffect=" + fireEffect + ",\n"
                + "  type=" + type + "\n"
                + "}";
    }
}
