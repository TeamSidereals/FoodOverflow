package io.github.teamsidereals.foodoverflow.registry;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.data.recipe.AgingChamberRecipe;
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
    }

    public static final RegistryObject<AgingChamberRecipe.Serializer> AGING_SERIALIZER
            = RECIPE_SERIALIZER.register("aging", AgingChamberRecipe.Serializer::new);

    public static IRecipeType<AgingChamberRecipe> AGING_RECIPE = new AgingChamberRecipe.AgingRecipeType();

}
