package io.github.teamsidereals.foodoverflow.item.equipment.shovel;

import io.github.teamsidereals.foodoverflow.registry.FoodOverflowItemsRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class CaramelShovel extends FoodOverflowShovel {
    public CaramelShovel(IItemTier p_i48460_1_, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(p_i48460_1_, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new TranslationTextComponent(
                        "\u00A7dTasty\u00A7r"
                )
        );
    }

    public void gummyDigger(World world, BlockPos pos){
        if (world.getBlockEntity(pos.above()) == null){
            pos = pos.above();
        }
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), FoodOverflowItemsRegister.GUMMY_WORM.get().getDefaultInstance());
        itemEntity.spawnAtLocation(itemEntity.getItem());
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState p_179218_3_, BlockPos pos, LivingEntity p_179218_5_) {
        if (!world.isClientSide){
            Random roll = new Random();
            if (roll.nextInt(100) < 10){
                gummyDigger(world, pos);
            }
        }
        return super.mineBlock(stack, world, p_179218_3_, pos, p_179218_5_);
    }

    public ActionResultType useOn(ItemUseContext p_195939_1_) {
        World world = p_195939_1_.getLevel();
        BlockPos blockpos = p_195939_1_.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (p_195939_1_.getClickedFace() == Direction.DOWN) {
            return ActionResultType.PASS;
        } else {
            PlayerEntity playerentity = p_195939_1_.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(world, blockpos, playerentity, p_195939_1_.getItemInHand(), net.minecraftforge.common.ToolType.SHOVEL);
            BlockState blockstate2 = null;
            if (blockstate1 != null && world.isEmptyBlock(blockpos.above())) {
                world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
                if (random.nextInt(100) < 10){
                    gummyDigger(world, blockpos);
                }
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!world.isClientSide()) {
                    world.levelEvent((PlayerEntity)null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(world, blockpos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }

            if (blockstate2 != null) {
                if (!world.isClientSide) {
                    world.setBlock(blockpos, blockstate2, 11);
                    if (playerentity != null) {
                        p_195939_1_.getItemInHand().hurtAndBreak(1, playerentity, (p_220041_1_) -> {
                            p_220041_1_.broadcastBreakEvent(p_195939_1_.getHand());
                        });
                    }
                }

                return ActionResultType.sidedSuccess(world.isClientSide);
            } else {
                return ActionResultType.PASS;
            }
        }
    }
}
