package io.github.teamsidereals.foodoverflow.registry;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.effect.StickyEffect;
import io.github.teamsidereals.foodoverflow.effect.TastyEffect;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodOverflowEffectsRegister {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, FoodOverflowMod.MODID);
    public static void init(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    public static final RegistryObject<Effect> TASTY = EFFECTS.register("tasty", () ->
            new TastyEffect(EffectType.HARMFUL, 16742291)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            "7107DE5E-7CE8-4030-940E-514C1F160890",
                            -0.05D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final RegistryObject<Effect> STICKY = EFFECTS.register("sticky", () ->
            new StickyEffect(EffectType.HARMFUL, 16742291)
    );
}
