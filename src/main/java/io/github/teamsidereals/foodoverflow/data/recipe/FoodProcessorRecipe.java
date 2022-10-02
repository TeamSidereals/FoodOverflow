package io.github.teamsidereals.foodoverflow.data.recipe;

import com.google.gson.JsonObject;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowBlocksRegister;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowRecipeTypesRegister;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FoodProcessorRecipe implements IFoodProcessorRecipe{

    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient additionInput;
    private final Ingredient input;
    private final int processingTime;

    public FoodProcessorRecipe(ResourceLocation id, ItemStack output, Ingredient additionOutput, Ingredient input, int processingTime) {
        this.id = id;
        this.output = output;
        this.additionInput = additionOutput;
        this.input = input;
        this.processingTime = processingTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(null, this.input, this.additionInput);
    }

    public ItemStack getIcon() {
        return new ItemStack(FoodOverflowBlocksRegister.FOOD_PROCESSOR.get());
    }

    public int getProcessingTime(){
        return this.processingTime;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        if (inv.getItem(0).isEmpty()) {
            return false;
        }
        if (inv.getItem(1).isEmpty()) {
            return this.input.test(inv.getItem(0)) && this.additionInput.test(Items.AIR.getDefaultInstance());
        }
        else {
            return this.input.test(inv.getItem(0)) && this.additionInput.test(inv.getItem(1));
        }
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return output;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Nullable
    public Ingredient getAdditionInput(){
        return additionInput;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return FoodOverflowRecipeTypesRegister.FOOD_PROCESSOR_SERIALIZER.get();
    }

    public static class FoodProcessorRecipeType implements IRecipeType<FoodProcessorRecipe> {
        @Override
        public String toString() {
            return FoodProcessorRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<FoodProcessorRecipe> {

        @Override
        public FoodProcessorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);
            Ingredient additionInput = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "addition input"));
            Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
            int processingTime = JSONUtils.getAsInt(json, "time");

            return new FoodProcessorRecipe(recipeId, output, additionInput,
                    input, processingTime);
        }

        @Nullable
        @Override
        public FoodProcessorRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient additionInput = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();

            return new FoodProcessorRecipe(recipeId, output, additionInput,
                    input, 0);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, FoodProcessorRecipe recipe) {
            Ingredient input = recipe.getIngredients().get(0);
            Ingredient additionInput = recipe.getIngredients().get(1);
            input.toNetwork(buffer);
            additionInput.toNetwork(buffer);

            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
