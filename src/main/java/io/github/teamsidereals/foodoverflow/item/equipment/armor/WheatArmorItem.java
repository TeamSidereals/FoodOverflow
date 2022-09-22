package io.github.teamsidereals.foodoverflow.item.equipment.armor;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WheatArmorItem extends FoodOverflowArmorItem {

    public WheatArmorItem(IArmorMaterial p_i48534_1_, EquipmentSlotType p_i48534_2_, Properties p_i48534_3_) {
        super(p_i48534_1_, p_i48534_2_, p_i48534_3_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new TranslationTextComponent(
                        "\u00A7b-Full set bonus-\u00A7r"
                )
        );
        tooltip.add(new TranslationTextComponent(
                        "\u00A7a50% deflect arrow fly to you\u00A7r"
                )
        );
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if(!world.isClientSide()) {
            if(hasFullSetArmor(player)) {
                Random roll = new Random();
                if (roll.nextInt(100) < 50){
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                            player.getX() - 2D, player.getY() - 1.5D, player.getZ() - 2D,
                            player.getX() + 2D, player.getY() + 2.5D, player.getZ() + 2D);
                    List<Entity> entities = world.getEntities(player,
                            axisAlignedBB);
                    for (Entity e : entities){
                        if (e instanceof AbstractArrowEntity){
                            e.setDeltaMovement(e.getDeltaMovement().reverse().add(e.getDeltaMovement()).x, e.getDeltaMovement().y, e.getDeltaMovement().reverse().add(e.getDeltaMovement()).z);
                        }
                    }
                }
            }
        }
        super.onArmorTick(stack, world, player);
    }

    private boolean hasFullSetArmor(PlayerEntity player) {
        ItemStack boots = player.inventory.getArmor(0);
        ItemStack leggings = player.inventory.getArmor(1);
        ItemStack chestplate = player.inventory.getArmor(2);
        ItemStack helmet = player.inventory.getArmor(3);

        if (helmet.getItem() instanceof WheatArmorItem
        && chestplate.getItem() instanceof WheatArmorItem
        && leggings.getItem() instanceof WheatArmorItem
        && boots.getItem() instanceof WheatArmorItem){
            return true;
        }
        else {
            return false;
        }
    }
}
