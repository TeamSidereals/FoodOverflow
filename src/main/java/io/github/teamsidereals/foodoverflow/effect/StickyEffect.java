package io.github.teamsidereals.foodoverflow.effect;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class StickyEffect extends FoodOverflowEffect{
    public StickyEffect(EffectType p_i50391_1_, int p_i50391_2_) {
        super(p_i50391_1_, p_i50391_2_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
        World world = entity.level;
        boolean isLivingNearby = false;
        BlockPos entityBlockPos = entity.blockPosition();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entity.getX() - 1D, entity.getY() - 0.5D, entity.getZ() - 1D,
                entity.getX() + 1D, entity.getY() + 1.5D, entity.getZ() + 1D);
        List<Entity> entities = world.getEntities(entity, axisAlignedBB);
        for (Entity e : entities){
            if (e instanceof LivingEntity){
                isLivingNearby = true;
            }
        }
        if (isLivingNearby
                || world.getBlockState(entityBlockPos.above().north()).getBlock() != Blocks.AIR
                || world.getBlockState(entityBlockPos.above().south()).getBlock() != Blocks.AIR
                || world.getBlockState(entityBlockPos.above().east()).getBlock() != Blocks.AIR
                || world.getBlockState(entityBlockPos.above().west()).getBlock() != Blocks.AIR){
            Vector3d vector3d = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vector3d(vector3d.x / 3, vector3d.y, vector3d.z / 3));
            if (entity.position().y > entityBlockPos.getY()){
                doSlideMovement(entity);
            }
        }
    }

    private void doSlideMovement(Entity p_226938_1_) {
        Vector3d vector3d = p_226938_1_.getDeltaMovement();
        if (vector3d.y < -0.13D) {
            double d0 = -0.05D / vector3d.y;
            p_226938_1_.setDeltaMovement(new Vector3d(vector3d.x * d0, -0.05D, vector3d.z * d0));
        } else {
            p_226938_1_.setDeltaMovement(new Vector3d(vector3d.x, -0.05D, vector3d.z));
        }

        p_226938_1_.fallDistance = 0.0F;
    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }
}
