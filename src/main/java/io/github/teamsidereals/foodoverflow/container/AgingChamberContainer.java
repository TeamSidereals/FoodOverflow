package io.github.teamsidereals.foodoverflow.container;

import io.github.teamsidereals.foodoverflow.registry.FoodOverflowBlocksRegister;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowContainersRegister;
import io.github.teamsidereals.foodoverflow.titleentity.AgingChamberTileEntity;
import io.github.teamsidereals.foodoverflow.utils.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class AgingChamberContainer extends Container {
    private final AgingChamberTileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    public IntReferenceHolder[] agingTime = new IntReferenceHolder[4];
    public IntReferenceHolder[] agingProgress = new IntReferenceHolder[4];

    public AgingChamberContainer(int windowId, World world, BlockPos pos,
                                       PlayerInventory playerInventory, PlayerEntity player) {
        super(FoodOverflowContainersRegister.AGING_CHAMBER_CONTAINER.get(), windowId);
        this.tileEntity = (AgingChamberTileEntity) world.getBlockEntity(pos);
        playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 92);

        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 26, 18));
                addSlot(new AgingChamberResultSlot(h, 1, 26, 62));
                addSlot(new SlotItemHandler(h, 2, 62, 18));
                addSlot(new AgingChamberResultSlot(h, 3, 62, 62));
                addSlot(new SlotItemHandler(h, 4, 98, 18));
                addSlot(new AgingChamberResultSlot(h, 5, 98, 62));
                addSlot(new SlotItemHandler(h, 6, 134, 18));
                addSlot(new AgingChamberResultSlot(h, 7, 134, 62));

            });
            this.addDataSlot(agingTime[0] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingTime[0],
                    value -> this.tileEntity.agingTime[0] = value));
            this.addDataSlot(agingTime[1] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingTime[1],
                    value -> this.tileEntity.agingTime[1] = value));
            this.addDataSlot(agingTime[2] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingTime[2],
                    value -> this.tileEntity.agingTime[2] = value));
            this.addDataSlot(agingTime[3] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingTime[3],
                    value -> this.tileEntity.agingTime[3] = value));

            this.addDataSlot(agingProgress[0] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingProgress[0],
                    value -> this.tileEntity.agingProgress[0] = value));
            this.addDataSlot(agingProgress[1] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingProgress[1],
                    value -> this.tileEntity.agingProgress[1] = value));
            this.addDataSlot(agingProgress[2] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingProgress[2],
                    value -> this.tileEntity.agingProgress[2] = value));
            this.addDataSlot(agingProgress[3] = new FunctionalIntReferenceHolder(() -> this.tileEntity.agingProgress[3],
                    value -> this.tileEntity.agingProgress[3] = value));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getAgingProgress(int slot){
        if (tileEntity.agingTime[slot] != 0 && tileEntity.agingProgress[slot] != 0){
            return tileEntity.agingProgress[slot] * 24 / tileEntity.agingTime[slot];
        }
        return 0;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerIn, FoodOverflowBlocksRegister.AGING_CHAMBER.get());
    }


    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 8;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }
}
