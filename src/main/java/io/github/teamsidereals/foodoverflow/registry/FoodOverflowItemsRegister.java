package io.github.teamsidereals.foodoverflow.registry;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.item.FoodOverflowItemGroup;
import io.github.teamsidereals.foodoverflow.item.equipment.FoodOverflowItemTier;
import io.github.teamsidereals.foodoverflow.item.equipment.sword.CaramelSword;
import io.github.teamsidereals.foodoverflow.item.equipment.sword.FoodOverflowSword;
import io.github.teamsidereals.foodoverflow.item.food.savory.FoodOverflowSavoryItem;
import io.github.teamsidereals.foodoverflow.item.food.sweet.FoodOverflowSweetItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodOverflowItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FoodOverflowMod.MODID);
    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> CARAMEL = ITEMS.register("caramel", () ->
            new FoodOverflowSweetItem(
                    new Item.Properties().tab(FoodOverflowItemGroup.FOOD_OVERFLOW_GROUP)
            )
    );

    public static final RegistryObject<Item> CARAMEL_BAR = ITEMS.register("caramel_bar", () ->
            new FoodOverflowSweetItem(
                    new Item.Properties().tab(FoodOverflowItemGroup.FOOD_OVERFLOW_GROUP)
            )
    );
//(harvestLevel, maxUse, efficiency, attackDamage, enchantability, repairMaterial)
//    CARAMEL(1, 100, 4.5F, 1.5F, 8, ()
//            -> Ingredient.of(FoodOverflowItemsRegister.CARAMEL_BAR.get()));

    public static final RegistryObject<Item> CARAMEL_SWORD = ITEMS.register("caramel_sword", () ->
            new CaramelSword(FoodOverflowItemTier.CARAMEL, 3, -2.2F,
                    new Item.Properties().tab(FoodOverflowItemGroup.FOOD_OVERFLOW_GROUP)
            )
    );

    public static final RegistryObject<Item> LOLLIPOP = ITEMS.register("lollipop", () ->
            new FoodOverflowSweetItem(
                    new Item.Properties().tab(FoodOverflowItemGroup.FOOD_OVERFLOW_GROUP)
                            .food(new Food.Builder()
                                    .nutrition(1)
                                    .saturationMod(1f)
                                    .fast()
                                    .build())
            )
    );

    public static final RegistryObject<Item> SANDWICH = ITEMS.register("sandwich", () ->
            new FoodOverflowSavoryItem(
                    new Item.Properties().tab(FoodOverflowItemGroup.FOOD_OVERFLOW_GROUP)
                            .food(new Food.Builder()
                                    .nutrition(10)
                                    .saturationMod(0.8f)
                                    .build())
            )
    );

    public static final RegistryObject<Item> KELP_SOUP = ITEMS.register("kelp_soup", () ->
            new FoodOverflowSavoryItem(
                    new Item.Properties().tab(FoodOverflowItemGroup.FOOD_OVERFLOW_GROUP)
                            .food(new Food.Builder()
                                    .nutrition(9)
                                    .saturationMod(0.6f)
                                    .build())
            ).alsoHealthy()
    );
}