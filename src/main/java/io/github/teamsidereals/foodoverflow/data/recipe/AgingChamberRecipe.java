package io.github.teamsidereals.foodoverflow.data.recipe;

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
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class AgingChamberRecipe implements IAgingChamberRecipe{

    private final ResourceLocation id;
    private final ItemStack output;
    private Ingredient input;
    private final int agingTime;

    public AgingChamberRecipe(ResourceLocation id, ItemStack output, Ingredient input, int agingTime) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.agingTime = agingTime;
    }

    public boolean matchesSlot(IInventory inv, int slot) {
        if (this.input.test(inv.getItem(slot))) {
            return true;
        }
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(null, this.input);
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return output;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public int getAgingTime(){
        return this.agingTime;
    }

    public ItemStack getIcon() {
        return new ItemStack(FoodOverflowBlocksRegister.AGING_CHAMBER.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return FoodOverflowRecipeTypesRegister.AGING_SERIALIZER.get();
    }

    public static class AgingRecipeType implements IRecipeType<AgingChamberRecipe> {
        @Override
        public String toString() {
            return AgingChamberRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<AgingChamberRecipe> {

        @Override
        public AgingChamberRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);
            Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
            int agingTime = JSONUtils.getAsInt(json, "time");

            return new AgingChamberRecipe(recipeId, output,
                    input, agingTime);
        }

        @Nullable
        @Override
        public AgingChamberRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();

            return new AgingChamberRecipe(recipeId, output,
                    input, 0);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, AgingChamberRecipe recipe) {
            Ingredient input = recipe.getIngredients().get(0);
            input.toNetwork(buffer);

            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
