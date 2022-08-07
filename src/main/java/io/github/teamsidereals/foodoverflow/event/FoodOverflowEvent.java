package io.github.teamsidereals.foodoverflow.event;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.item.food.savory.FoodOverflowSavoryItem;
import io.github.teamsidereals.foodoverflow.item.food.sweet.FoodOverflowSweetItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = FoodOverflowMod.MODID)
public class FoodOverflowEvent {
    private static final List<Item> vanillaSavoryFood = new ArrayList<>(
            Arrays.asList(
                    Items.COOKED_BEEF, Items.COOKED_COD, Items.COOKED_CHICKEN,
                    Items.COOKED_MUTTON, Items.COOKED_RABBIT, Items.COOKED_PORKCHOP,
                    Items.COOKED_SALMON, Items.RABBIT_STEW
            )
    );

    private static final List<Item> vanillaSweetFood = new ArrayList<>(
            Arrays.asList(
                    Items.COOKIE, Items.SWEET_BERRIES, Items.PUMPKIN_PIE
            )
    );

    private static List<String> playerList = new ArrayList<>();
    private static List<Integer> savoryFoodCount = new ArrayList<>();
    private static List<Integer> sweetFoodCount = new ArrayList<>();
    private static List<Integer> sugarRushTick = new ArrayList<>();

    @SubscribeEvent
    public static void setTickWhenJoin(EntityJoinWorldEvent event){
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
                if (vanillaSavoryFood.contains(event.getItem().getItem())
                        || event.getItem().getItem() instanceof FoodOverflowSavoryItem){
                    FullBelly(player);
                }
                if (vanillaSweetFood.contains(event.getItem().getItem())
                        || event.getItem().getItem() instanceof FoodOverflowSweetItem){
                    SugarRush(player);
                }
            }
        }
    }

    public static void FullBelly(PlayerEntity player){
        savoryFoodCount.set(
                playerList.indexOf(player.getScoreboardName()),
                savoryFoodCount.get(playerList.indexOf(player.getScoreboardName())) + 1
        );
        if (savoryFoodCount.get(playerList.indexOf(player.getScoreboardName())) == 5){
            player.displayClientMessage(new TranslationTextComponent("Your stomach is full, you feel stronger").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.DARK_GREEN), true);
            player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200));
            player.addEffect(new EffectInstance(Effects.ABSORPTION, 200));
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
            savoryFoodCount.set(playerList.indexOf(player.getScoreboardName()), 0);
        }
    }

    public static void SugarRush(PlayerEntity player){
        sweetFoodCount.set(
                playerList.indexOf(player.getScoreboardName()),
                sweetFoodCount.get(playerList.indexOf(player.getScoreboardName())) + 1
        );
        if (sweetFoodCount.get(playerList.indexOf(player.getScoreboardName())) == 5){
            player.displayClientMessage(new TranslationTextComponent("Your sugar level is high, you feel full of energy").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.YELLOW), true);
            player.addEffect(new EffectInstance(Effects.DIG_SPEED, 200));
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 200));
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
}
