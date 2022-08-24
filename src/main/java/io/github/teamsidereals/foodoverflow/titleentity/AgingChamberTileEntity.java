package io.github.teamsidereals.foodoverflow.titleentity;

import io.github.teamsidereals.foodoverflow.data.recipe.AgingChamberRecipe;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowRecipeTypesRegister;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowTileEntitiesRegister;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
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

public class AgingChamberTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public int[] agingTime = {0, 0, 0, 0};
    public int[] agingProgress = {0, 0, 0, 0};

    public AgingChamberTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public AgingChamberTileEntity() {
        this(FoodOverflowTileEntitiesRegister.AGING_CHAMBER_TILE.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.agingTime[0] = nbt.getInt("AgingTime0");
        this.agingTime[1] = nbt.getInt("AgingTime1");
        this.agingTime[2] = nbt.getInt("AgingTime2");
        this.agingTime[3] = nbt.getInt("AgingTime3");
        this.agingProgress[0] = nbt.getInt("AgingProgress0");
        this.agingProgress[1] = nbt.getInt("AgingProgress1");
        this.agingProgress[2] = nbt.getInt("AgingProgress2");
        this.agingProgress[3] = nbt.getInt("AgingProgress3");
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("AgingTime0", this.agingTime[0]);
        compound.putInt("AgingTime1", this.agingTime[1]);
        compound.putInt("AgingTime2", this.agingTime[2]);
        compound.putInt("AgingTime3", this.agingTime[3]);
        compound.putInt("AgingProgress0", this.agingProgress[0]);
        compound.putInt("AgingProgress1", this.agingProgress[1]);
        compound.putInt("AgingProgress2", this.agingProgress[2]);
        compound.putInt("AgingProgress3", this.agingProgress[3]);
        return super.save(compound);
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    private void finishAging(ItemStack output, int slot) {
        itemHandler.extractItem(slot, 1, false);
        itemHandler.insertItem(slot + 1, output, false);
    }

    @Override
    public void tick() {
        if(level.isClientSide)
            return;
        boolean isChanged = false;
        for (int i = 0; i < 4; i++){
            if (this.getRecipe(this.itemHandler.getStackInSlot(i << 1), i << 1) != null
                    && this.getRecipe(this.itemHandler.getStackInSlot(i << 1), i << 1).getAgingTime() > 0) {

                this.agingTime[i] = this.getRecipe(this.itemHandler.getStackInSlot(i << 1), i << 1).getAgingTime();

                if (this.agingProgress[i] < agingTime[i]){
                    this.agingProgress[i]++;
                }
                else {
                    if (this.itemHandler.getStackInSlot((i << 1) + 1).getCount() < 64) {
                        ItemStack output = this.getRecipe(this.itemHandler.getStackInSlot(i << 1), i << 1).getResultItem();
                        if (this.itemHandler.getStackInSlot((i << 1) + 1).getItem() == output.getItem()
                                || this.itemHandler.getStackInSlot((i << 1) + 1).getItem() == Items.AIR){
                            finishAging(output, i << 1);
                            this.agingProgress[i] = 0;
                        }
                    }
                }
                isChanged = true;
            }
            else {
                this.agingProgress[i] = 0;
            }
        }
        if (isChanged){
            setChanged();
        }
    }

    @Nullable
    private AgingChamberRecipe getRecipe(ItemStack stack, int slot) {
        if (stack == null) {
            return null;
        }

        Set<IRecipe<?>> recipes = findRecipesByType(FoodOverflowRecipeTypesRegister.AGING_RECIPE, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            AgingChamberRecipe recipe = (AgingChamberRecipe) iRecipe;
            if (recipe.matchesSlot(new RecipeWrapper(this.itemHandler), this.level, slot)) {
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
