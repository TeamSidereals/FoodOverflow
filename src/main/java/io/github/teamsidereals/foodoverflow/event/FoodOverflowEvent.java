package io.github.teamsidereals.foodoverflow.event;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.effect.FoodOverflowEffect;
import io.github.teamsidereals.foodoverflow.effect.TastyEffect;
import io.github.teamsidereals.foodoverflow.item.food.FoodOverflowFoodItem;
import io.github.teamsidereals.foodoverflow.item.food.bland.FoodOverflowBlandItem;
import io.github.teamsidereals.foodoverflow.item.food.healthy.FoodOverflowHealthyItem;
import io.github.teamsidereals.foodoverflow.item.food.savory.FoodOverflowSavoryItem;
import io.github.teamsidereals.foodoverflow.item.food.sweet.FoodOverflowSweetItem;
import io.github.teamsidereals.foodoverflow.registry.FoodOverflowEffectsRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = FoodOverflowMod.MODID)
public class FoodOverflowEvent {
    private static final List<String> additionSavoryFood = new ArrayList<>(
            Arrays.asList(
                    "minecraft:cooked_beef", "minecraft:cooked_cod", "minecraft:cooked_chicken",
                    "minecraft:cooked_mutton", "minecraft:cooked_rabbit", "minecraft:cooked_porkchop",
                    "minecraft:cooked_salmon", "minecraft:rabbit_stew"
            )
    );

    private static final List<String> additionSweetFood = new ArrayList<>(
            Arrays.asList(
                    "minecraft:cookie", "minecraft:pumpkin_pie"
            )
    );

    private static final List<String> additionHealthyFood = new ArrayList<>(
            Arrays.asList(
                    "minecraft:sweet_berries", "minecraft:apple", "minecraft:beetroot",
                    "minecraft:beetroot_soup", "minecraft:carrot", "minecraft:melon_slice",
                    "minecraft:dried_kelp", "minecraft:mushroom_stew"
            )
    );

    private static final List<String> additionBlandFood = new ArrayList<>(
            Arrays.asList(
                    "minecraft:bread", "minecraft:baked_potato", "foodoverflow:potato"
            )
    );

    private static List<String> playerList = new ArrayList<>();
    private static List<Integer> savoryFoodCount = new ArrayList<>();
    private static List<Integer> sweetFoodCount = new ArrayList<>();
    private static List<Integer> sugarRushTick = new ArrayList<>();
    private static List<Integer> healthyFoodCount = new ArrayList<>();
    private static List<Integer> blandFoodCount = new ArrayList<>();

    @SubscribeEvent
    public static void setDataWhenJoin(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        World world = event.getWorld();
        if (!world.isClientSide){
            if (entity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entity;
                if (!playerList.contains(player.getScoreboardName())){
                    playerList.add(player.getScoreboardName());
                    savoryFoodCount.add(0);
                    sweetFoodCount.add(0);
                    sugarRushTick.add(0);
                    healthyFoodCount.add(0);
                    blandFoodCount.add(0);
                    System.out.println(playerList);
                }
            }
        }
    }

    @SubscribeEvent
    public static void GiveBuffFromEatingFood(LivingEntityUseItemEvent event){
        World world = event.getEntity().level;
        if(!world.isClientSide && event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player.getUseItemRemainingTicks() == 1){
                if (additionSavoryFood.contains(event.getItem().getItem().getRegistryName().toString())
                        || ((event.getItem().getItem()) instanceof FoodOverflowFoodItem
                                        && ((FoodOverflowFoodItem) event.getItem().getItem()).isSavory)){
                    FullBelly(player, event.getItem().getItem());
                }
                if (additionSweetFood.contains(event.getItem().getItem().getRegistryName().toString())
                        || ((event.getItem().getItem()) instanceof FoodOverflowFoodItem
                        && ((FoodOverflowFoodItem) event.getItem().getItem()).isSweet)){
                    SugarRush(player, event.getItem().getItem());
                }
                if (additionHealthyFood.contains(event.getItem().getItem().getRegistryName().toString())
                        || ((event.getItem().getItem()) instanceof FoodOverflowFoodItem
                        && ((FoodOverflowFoodItem) event.getItem().getItem()).isHealthy)){
                    Healthy(player, event.getItem().getItem());
                }
                if (additionBlandFood.contains(event.getItem().getItem().getRegistryName().toString())
                        || ((event.getItem().getItem()) instanceof FoodOverflowFoodItem
                        && ((FoodOverflowFoodItem) event.getItem().getItem()).isBland)){
                    Neutralize(player, event.getItem().getItem());
                }
            }
        }
    }

    public static void FullBelly(PlayerEntity player, Item item){
        savoryFoodCount.set(
                playerList.indexOf(player.getScoreboardName()),
                savoryFoodCount.get(playerList.indexOf(player.getScoreboardName())) + 1
        );
        if (savoryFoodCount.get(playerList.indexOf(player.getScoreboardName())) == 5){
            player.displayClientMessage(new TranslationTextComponent("Your stomach is full, you feel stronger").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.RED), true);
            if (item instanceof FoodOverflowFoodItem){
                player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200 + ((FoodOverflowFoodItem) item).DamageResTimeModify, ((FoodOverflowFoodItem) item).DamageResLevelModify));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 200 + ((FoodOverflowFoodItem) item).AbsorptionTimeModify, ((FoodOverflowFoodItem) item).AbsorptionLevelModify));
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200 + ((FoodOverflowFoodItem) item).SpeedDownTimeModify, ((FoodOverflowFoodItem) item).SpeedDownLevelModify));
            }
            else {
                player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 200 ));
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
            }
            savoryFoodCount.set(playerList.indexOf(player.getScoreboardName()), 0);
        }
    }

    public static void SugarRush(PlayerEntity player, Item item){
        sweetFoodCount.set(
                playerList.indexOf(player.getScoreboardName()),
                sweetFoodCount.get(playerList.indexOf(player.getScoreboardName())) + 1
        );
        if (sweetFoodCount.get(playerList.indexOf(player.getScoreboardName())) == 5){
            player.displayClientMessage(new TranslationTextComponent("Your sugar level is high, you feel full of energy").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.YELLOW), true);
            if (item instanceof FoodOverflowFoodItem){
                player.addEffect(new EffectInstance(Effects.DIG_SPEED, 200 + ((FoodOverflowFoodItem) item).HasteTimeModify, ((FoodOverflowFoodItem) item).HasteLevelModify));
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 200 + ((FoodOverflowFoodItem) item).SpeedUpTimeModify, ((FoodOverflowFoodItem) item).SpeedUpLevelModify));
            }
            else {
                player.addEffect(new EffectInstance(Effects.DIG_SPEED, 200));
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 200));
            }
            sweetFoodCount.set(playerList.indexOf(player.getScoreboardName()), 0);
            sugarRushTick.set(playerList.indexOf(player.getScoreboardName()), player.tickCount);
        }
    }

    @SubscribeEvent
    public static void SugarRushExpire(TickEvent.PlayerTickEvent event){
        World world = event.player.level;
        PlayerEntity player = event.player;
        if (!world.isClientSide && player.tickCount != 200 &&
                (player.tickCount - sugarRushTick.get(playerList.indexOf(player.getScoreboardName())) == 200)){
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1));
            player.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 100, 1));
            sugarRushTick.set(playerList.indexOf(player.getScoreboardName()), 0);
        }
    }

    public static void Healthy(PlayerEntity player, Item item){
        healthyFoodCount.set(
                playerList.indexOf(player.getScoreboardName()),
                healthyFoodCount.get(playerList.indexOf(player.getScoreboardName())) + 1
        );
        if (healthyFoodCount.get(playerList.indexOf(player.getScoreboardName())) == 5){
            player.displayClientMessage(new TranslationTextComponent("Your body feel pleased from healthy food").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.DARK_GREEN), true);
            if (item instanceof FoodOverflowFoodItem){
                player.addEffect(new EffectInstance(Effects.REGENERATION, 100 + ((FoodOverflowFoodItem) item).RegenerationTimeModify, ((FoodOverflowFoodItem) item).RegenerationLevelModify));
                player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 100 + ((FoodOverflowFoodItem) item).NightVisionTimeModify, ((FoodOverflowFoodItem) item).NightVisionLevelModify));

            }
            else {
                player.addEffect(new EffectInstance(Effects.REGENERATION, 100));
                player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 100));
            }
            healthyFoodCount.set(playerList.indexOf(player.getScoreboardName()), 0);
        }
    }

    public static void Neutralize(PlayerEntity player, Item item){
        blandFoodCount.set(
                playerList.indexOf(player.getScoreboardName()),
                blandFoodCount.get(playerList.indexOf(player.getScoreboardName())) + 1
        );
        if (blandFoodCount.get(playerList.indexOf(player.getScoreboardName())) == 10){
            player.displayClientMessage(new TranslationTextComponent("You feel fine, cleanse all bad effect").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.WHITE), true);
            Map<Effect, EffectInstance> effectMap = player.getActiveEffectsMap();
            List<Effect> badEffectList = new ArrayList<>();
            for (Map.Entry<Effect, EffectInstance> tracker : effectMap.entrySet()){
                if (!tracker.getKey().isBeneficial()){
                    badEffectList.add(tracker.getKey());
                }
            }
            for (Effect badEffect : badEffectList){
                player.removeEffect(badEffect);
            }
            blandFoodCount.set(playerList.indexOf(player.getScoreboardName()), 0);
        }
    }

    @SubscribeEvent
    public static void TastyEvent(LivingHurtEvent event){
        if (!event.getEntity().level.isClientSide){
            if (event.getEntity() instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) event.getEntity();
                if (entity.hasEffect(FoodOverflowEffectsRegister.TASTY.get())
                && event.getSource().getEntity() != null
                && event.getSource().getEntity() instanceof LivingEntity){
                    LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                    Random roll = new Random();
                    if (roll.nextFloat() < 0.2F){
                        attacker.heal(2.0F);
                    }
                }
            }
        }
    }
}
