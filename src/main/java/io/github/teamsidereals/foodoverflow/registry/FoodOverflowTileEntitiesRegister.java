package io.github.teamsidereals.foodoverflow.registry;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.titleentity.AgingChamberTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodOverflowTileEntitiesRegister {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FoodOverflowMod.MODID);

    public static void init(IEventBus eventBus){
        TILE_ENTITIES.register(eventBus);
    }

    public static final RegistryObject<TileEntityType<AgingChamberTileEntity>> AGING_CHAMBER_TILE =
            TILE_ENTITIES.register("aging_chamber_tile", () -> TileEntityType.Builder.of(
                    AgingChamberTileEntity::new, FoodOverflowBlocksRegister.AGING_CHAMBER.get()).build(null)
            );
}
