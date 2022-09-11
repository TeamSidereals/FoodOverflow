package io.github.teamsidereals.foodoverflow.event;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.event.loottable.PigFatLootModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = FoodOverflowMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FoodOverflowEventBusEvent {
    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>>
                                                           event) {
        event.getRegistry().registerAll(
                new PigFatLootModifier.Serializer().setRegistryName
                        (new ResourceLocation(FoodOverflowMod.MODID,"pig_fat_from_pig"))
        );
    }
}
