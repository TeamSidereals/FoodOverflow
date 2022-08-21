package io.github.teamsidereals.foodoverflow.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AgingChamberResultSlot extends SlotItemHandler {
    public AgingChamberResultSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {
        return false;
    }
}
