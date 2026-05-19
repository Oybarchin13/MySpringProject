package uz.pdp.foodswift.model.entity.enums;

import lombok.Getter;

@Getter

public enum FoodCategory {
    SALADS("Salatlar"),
    SOUPS("Sho'rvalar"),
    KEBABS("Kebablar"),
    MEAT_DISHES("Go'shtli taomlar"),
    CHICKEN_DISHES("Tovuqli taomlar"),
    FISH_DISHES("Baliqli taomlar"),
    SIDE_DISHES("Garnirlar"),
    BURGERS("Burgerlar"),
    PIZZA("Pizza"),
    DRINKS("Ichimliklar"),
    SWEETS("Shirinliklar");

    private final String displayName;

    FoodCategory(String displayName) {
        this.displayName = displayName;
    }

}
