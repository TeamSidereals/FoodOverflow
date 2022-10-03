package io.github.teamsidereals.foodoverflow.titleentity;

import io.github.teamsidereals.foodoverflow.data.recipe.OvenRecipe;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowRecipeTypesRegister;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowTileEntitiesRegister;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class OvenTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public IIntArray ovenData = new IntArray(2);

    public OvenTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public OvenTileEntity() {
        this(FoodOverflowTileEntitiesRegister.OVEN_TILE.get());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(8) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        ovenData.set(0,nbt.getInt("ovenTime"));
        ovenData.set(1,nbt.getInt("ovenProgress"));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("ovenTime", ovenData.get(0));
        compound.putInt("ovenProgress", ovenData.get(1));
        return super.save(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    private void finishCooking(ItemStack output, ItemStack additionOutput) {
        for (int i = 0; i < itemHandler.getSlots() - 2; i++){
            ItemStack slotItem = itemHandler.getStackInSlot(i);
            if (slotItem == null
                    || itemHandler.getStackInSlot(i).getItem() == Items.AIR){
                continue;
            }
            itemHandler.extractItem(i,1,false);
            if (slotItem.getItem() == Items.WATER_BUCKET || slotItem.getItem() == Items.MILK_BUCKET){
                itemHandler.insertItem(i, Items.BUCKET.getDefaultInstance(), false);
            }
        }
        itemHandler.insertItem(6, output, false);
        if (additionOutput.getItem() != Items.AIR){
            itemHandler.insertItem(7, additionOutput, false);
        }
    }

    @Override
    public void tick() {
        if(level.isClientSide)
            return;
        boolean isChanged = false;
        if (this.getRecipe() != null){
            this.ovenData.set(0, this.getRecipe().getCookingTime());
            if (ovenData.get(1) < ovenData.get(0)){
                ovenData.set(1,ovenData.get(1) + 1);
            }
            else {
                ItemStack output = this.getRecipe().getResultItem();
                ItemStack additionOutput = this.getRecipe().getAdditionOutput();
                if (this.itemHandler.getStackInSlot(6).getCount() + output.getCount() <= 64
                && this.itemHandler.getStackInSlot(7).getCount() + additionOutput.getCount() <= 64) {
                        if ((this.itemHandler.getStackInSlot(6).getItem() == output.getItem()
                                || this.itemHandler.getStackInSlot(6).getItem() == Items.AIR)
                        && (this.itemHandler.getStackInSlot(7).getItem() == additionOutput.getItem()
                                || this.itemHandler.getStackInSlot(7).getItem() == Items.AIR)){
                            finishCooking(output, additionOutput);
                            this.ovenData.set(1,0);
                        }
                    }
            }
            isChanged = true;
        }
        else {
            this.ovenData.set(0,0);
            this.ovenData.set(1,0);
        }
        if (isChanged){
            setChanged();
        }
    }

    @Nullable
    private OvenRecipe getRecipe() {
        Set<IRecipe<?>> recipes = findRecipesByType(FoodOverflowRecipeTypesRegister.OVEN_RECIPE, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            OvenRecipe recipe = (OvenRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.itemHandler), this.level)) {
                return recipe;
            }
        }

        return null;
    }

    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }
}
