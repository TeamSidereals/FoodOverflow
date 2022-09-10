package io.github.teamsidereals.foodoverflow.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowBlocksRegister;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowRecipeTypesRegister;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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

public class OvenRecipe implements IOvenRecipe{

    private final ResourceLocation id;
    private final ItemStack output;
    private final ItemStack additionOutput;
    private NonNullList<Ingredient> inputs;
    private final int cookingTime;

    public OvenRecipe(ResourceLocation id, ItemStack output, ItemStack additionOutput, NonNullList<Ingredient> inputs, int cookingTime) {
        this.id = id;
        this.output = output;
        this.additionOutput = additionOutput;
        this.inputs = inputs;
        this.cookingTime = cookingTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    public ItemStack getIcon() {
        return new ItemStack(FoodOverflowBlocksRegister.OVEN.get());
    }

    public int getCookingTime(){
        return this.cookingTime;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;

        for(int j = 0; j < p_77569_1_.getContainerSize() - 2; ++j) {
            ItemStack itemstack = p_77569_1_.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        return i == (this.inputs.size()) &&
                RecipeMatcher.findMatches(inputs,  this.inputs) != null;
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return output;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public ItemStack getAdditionOutput(){
        return additionOutput.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return FoodOverflowRecipeTypesRegister.OVEN_SERIALIZER.get();
    }

    public static class OvenRecipeType implements IRecipeType<OvenRecipe> {
        @Override
        public String toString() {
            return OvenRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<OvenRecipe> {

        @Override
        public OvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);
            ItemStack additionOutput = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "addition"), true);
            NonNullList<Ingredient> inputs = itemsFromJson(JSONUtils.getAsJsonArray(json, "inputs"));
            int agingTime = JSONUtils.getAsInt(json, "time");

            return new OvenRecipe(recipeId, output, additionOutput,
                    inputs, agingTime);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray json) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public OvenRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(6, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            ItemStack additionOutput = buffer.readItem();

            return new OvenRecipe(recipeId, output, additionOutput,
                    inputs, 0);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, OvenRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
