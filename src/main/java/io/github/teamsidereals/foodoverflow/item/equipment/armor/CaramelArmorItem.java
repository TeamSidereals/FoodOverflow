package io.github.teamsidereals.foodoverflow.item.equipment.armor;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CaramelArmorItem extends FoodOverflowArmorItem {

    public CaramelArmorItem(IArmorMaterial p_i48534_1_, EquipmentSlotType p_i48534_2_, Properties p_i48534_3_) {
        super(p_i48534_1_, p_i48534_2_, p_i48534_3_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new TranslationTextComponent(
                        "\u00A7dTasty\u00A7r"
                )
        );
        tooltip.add(new TranslationTextComponent(
                        ""
                )
        );
        tooltip.add(new TranslationTextComponent(
                        "\u00A7b-Full set bonus-\u00A7r"
                )
        );
        if (!Screen.hasShiftDown()){
            tooltip.add(new TranslationTextComponent(
                            "Hold \u00A7eSHIFT\u00A7r for details"
                    )
            );
        }
        else {
            tooltip.add(new TranslationTextComponent(
                            "\u00A7aImmune Slowness and Mining Fatigue (consume durability)\u00A7r"
                    )
            );
        }
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        boolean triggerEffect = false;
        if(!world.isClientSide()) {
            if(hasFullSetArmor(player)) {
                if (player.getEffect(Effects.DIG_SLOWDOWN) != null){
                    player.removeEffect(Effects.DIG_SLOWDOWN);
                    triggerEffect = true;
                }
                if (player.getEffect(Effects.MOVEMENT_SLOWDOWN) != null){
                    player.removeEffect(Effects.MOVEMENT_SLOWDOWN);
                    triggerEffect = true;
                }
                if (triggerEffect){
                    ItemStack boots = player.inventory.getArmor(0);
                    ItemStack leggings = player.inventory.getArmor(1);
                    ItemStack chestplate = player.inventory.getArmor(2);
                    ItemStack helmet = player.inventory.getArmor(3);

                    helmet.setDamageValue(stack.getDamageValue() + 1);
                    chestplate.setDamageValue(stack.getDamageValue() + 1);
                    leggings.setDamageValue(stack.getDamageValue() + 1);
                    boots.setDamageValue(stack.getDamageValue() + 1);
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

        if (helmet.getItem() instanceof CaramelArmorItem
        && chestplate.getItem() instanceof CaramelArmorItem
        && leggings.getItem() instanceof CaramelArmorItem
        && boots.getItem() instanceof CaramelArmorItem){
            return true;
        }
        else {
            return false;
        }
    }
}
