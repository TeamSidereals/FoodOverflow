package io.github.teamsidereals.foodoverflow.item.food.special;

import io.github.teamsidereals.foodoverflow.item.food.FoodOverflowFoodItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class FortuneCookieItem extends FoodOverflowFoodItem {
    public FortuneCookieItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new TranslationTextComponent(
                        ""
                )
        );
        tooltip.add(new TranslationTextComponent(
                        "20% chance give you a random vanilla effect with random duration and level"
                )
        );
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide){
            Random roll = new Random();
            if (roll.nextInt(100) < 20){
                entity.addEffect(new EffectInstance(
                        Effect.byId(roll.nextInt(32) + 1),
                        (roll.nextInt(6) + 1) * 100,
                        roll.nextInt(3)));
            }
        }
        return entity.eat(world, stack);
    }
}
