package io.github.teamsidereals.foodoverflow.registry;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.data.recipe.AgingChamberRecipe;
import io.github.teamsidereals.foodoverflow.data.recipe.OvenRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodOverflowRecipeTypesRegister {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FoodOverflowMod.MODID);

    public static void init(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, AgingChamberRecipe.TYPE_ID, AGING_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, OvenRecipe.TYPE_ID, OVEN_RECIPE);
    }

    public static final RegistryObject<AgingChamberRecipe.Serializer> AGING_SERIALIZER
            = RECIPE_SERIALIZER.register("aging", AgingChamberRecipe.Serializer::new);

    public static IRecipeType<AgingChamberRecipe> AGING_RECIPE = new AgingChamberRecipe.AgingRecipeType();

    public static final RegistryObject<OvenRecipe.Serializer> OVEN_SERIALIZER
            = RECIPE_SERIALIZER.register("oven", OvenRecipe.Serializer::new);

    public static IRecipeType<OvenRecipe> OVEN_RECIPE = new OvenRecipe.OvenRecipeType();

}
