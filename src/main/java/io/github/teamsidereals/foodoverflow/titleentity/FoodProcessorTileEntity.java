package io.github.teamsidereals.foodoverflow.titleentity;

import io.github.teamsidereals.foodoverflow.data.recipe.FoodProcessorRecipe;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowRecipeTypesRegister;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowTileEntitiesRegister;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
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

public class FoodProcessorTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public IIntArray processorData = new IntArray(2);

    public FoodProcessorTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public FoodProcessorTileEntity() {
        this(FoodOverflowTileEntitiesRegister.FOOD_PROCESSOR_TILE.get());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {
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
        processorData.set(0,nbt.getInt("processorTime"));
        processorData.set(1,nbt.getInt("processorProgess"));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("processorTime", processorData.get(0));
        compound.putInt("processorProgess", processorData.get(1));
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

    private void finishProcessing(ItemStack output) {
        for (int i = 0; i < itemHandler.getSlots() - 1; i++){
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
        itemHandler.insertItem(2, output, false);
    }

    @Override
    public void tick() {
        if(level.isClientSide)
            return;
        boolean isChanged = false;
        if (this.getRecipe() != null){
            this.processorData.set(0, this.getRecipe().getProcessingTime());
            if (processorData.get(1) < processorData.get(0)){
                processorData.set(1, processorData.get(1) + 1);
            }
            else {
                if (this.itemHandler.getStackInSlot(2).getCount() < 64) {
                        ItemStack output = this.getRecipe().getResultItem();
                        if ((this.itemHandler.getStackInSlot(2).getItem() == output.getItem()
                                || this.itemHandler.getStackInSlot(2).getItem() == Items.AIR)){
                            finishProcessing(output);
                            this.processorData.set(1,0);
                        }
                    }
            }
            isChanged = true;
        }
        else {
            this.processorData.set(0,0);
            this.processorData.set(1,0);
        }
        if (isChanged){
            setChanged();
        }
    }

    @Nullable
    private FoodProcessorRecipe getRecipe() {
        Set<IRecipe<?>> recipes = findRecipesByType(FoodOverflowRecipeTypesRegister.FOOD_PROCESSOR_RECIPE, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            FoodProcessorRecipe recipe = (FoodProcessorRecipe) iRecipe;
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
