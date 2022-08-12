package io.github.teamsidereals.foodoverflow.item.food;

import net.minecraft.item.Item;

/*  Food saturation and hunger/nutrition formula

    Food register will have 2 value nutrition and saturationMod
    Saturation = nutrition * saturationMod * 2
    Example:
        Apple: nutrition = 4 ; saturationMod = 0.3 ; saturation = 4 * 0.3 * 2 = 2.4

    Note: Everything is point, 2 points = 1 bar. Player max nutrition and saturation value is 20 (10 bar)

 */
public class FoodOverflowFoodItem extends Item {
    public FoodOverflowFoodItem(Properties properties) {
        super(properties);
        this.isSweet = false;
        this.isSavory = false;
        this.isHealthy = false;
        this.isBland = false;

    }

    public boolean isSavory;
    public boolean isSweet;
    public boolean isHealthy;
    public boolean isBland;

    public FoodOverflowFoodItem alsoSavory(){
        this.isSavory = true;
        return this;
    }
    public FoodOverflowFoodItem alsoSweet(){
        this.isSweet = true;
        return this;
    }
    public FoodOverflowFoodItem alsoHealthy(){
        this.isHealthy = true;
        return this;
    }
    public FoodOverflowFoodItem alsoBland(){
        this.isBland = true;
        return this;
    }
}
