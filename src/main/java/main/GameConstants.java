package main;

public final class GameConstants {

    private GameConstants() {}

    //every magic number lives here. AI! when you modify this class either:
    //1-add new fields for new functions you introduced  
    //2- remove a field because it is no longer needed.
    //3- tweak a number because used specifically asked it.
    //otherwise dont touch any of the fields. Always keep the comments unless meaning of the field changed.
    
    public static final int DEFAULT_ITEM_BASE_PRICE = 5; // Default base price for items when not specified

    public static final int TRADE_DEFAULT_BASE_PRICE = 5; // Fallback price used if item base price is invalid or zero
    public static final int TRADE_BUY_DELTA = 2; // Amount subtracted from base price when player buys an item
    public static final int TRADE_SELL_DELTA = 2; // Amount added to base price when player sells an item
    public static final int TRADE_MIN_PRICE = 1; // Minimum possible price for any trade transaction

    public static final int PLAYER_START_FOOD = 5; // Starting food resource for the player
    public static final int PLAYER_START_FUEL = 5; // Starting fuel resource for the player
    public static final int PLAYER_START_MONEY = 20; // Starting money for the player

    public static final int VISITS_RANDOM_TRIGGER_THRESHOLD = 3; // Minimum total visits needed before random visits stop spawning
    public static final int VISITS_MAX_PER_DAY = 5; // Maximum total visits allowed in a single day
    public static final int VISITS_RANDOM_MAX = 2; // Maximum number of random visits generated in one resolution pass

    public static final int STACK_AMOUNT = 1; // Default quantity change when adding or removing items from stacks
    public static final int FUEL_BURN_COST = 1; // Amount of fuel consumed when burning fuel

    public static final int DAY_START = 1; // Initial day number when starting a new game

    public static final int SCRIPTED_DEFAULT_MIN_DELAY = 0; // Default minimum delay (in days) before scripted visit can trigger
    public static final int SCRIPTED_DEFAULT_MAX_DELAY = 0; // Default maximum delay (in days) before scripted visit can trigger

    public static final int VISITITEM_RARE_CHANCE = 4; // Chance denominator for rare visit items (1 in N chance)
    public static final int VISITITEM_ULTRA_RARE_CHANCE = 10; // Chance denominator for ultra rare visit items (1 in N chance)
    
    public static final int FOOD_PRICE = 3;
public static final int FUEL_PRICE = 5;
}