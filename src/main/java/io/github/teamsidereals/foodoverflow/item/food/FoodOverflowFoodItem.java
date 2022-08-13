package io.github.teamsidereals.foodoverflow.item.food;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        if (((FoodOverflowFoodItem) stack.getItem()).isSavory){
            tooltip.add(new TranslationTextComponent(
                    "\u00A7eSavory\u00A7r"
                )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isSweet){
            tooltip.add(new TranslationTextComponent(
                            "\u00A7cSweet\u00A7r"
                    )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isHealthy){
            tooltip.add(new TranslationTextComponent(
                            "\u00A7aHealthy\u00A7r"
                    )
            );
        }
        if (((FoodOverflowFoodItem) stack.getItem()).isBland){
            tooltip.add(new TranslationTextComponent(
                            "\u00A7fBland\u00A7r"
                    )
            );
        }
    }
}
