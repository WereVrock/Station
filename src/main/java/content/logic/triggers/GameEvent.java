package content.logic.triggers;

public enum GameEvent {
    VISIT_START, //implemented
    VISIT_APPEARS,//implemented
    VISIT_END,
    VISIT_CREATED,
    ITEM_SOLD_TO_CHARACTER,
    ITEM_BOUGHT_FROM_CHARACTER,
    FUEL_SOLD_TO_CHARACTER,
    FUEL_BOUGHT_FROM_CHARACTER, 
    FOOD_SOLD_TO_CHARACTER,
    FOOD_BOUGHT_FROM_CHARACTER

    //Adding or changing here is enough but json must also accomodate. Also need to write firing logic.
}
