package io.github.teamsidereals.foodoverflow.registry;

import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.container.AgingChamberContainer;
import io.github.teamsidereals.foodoverflow.container.OvenContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodOverflowContainersRegister {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, FoodOverflowMod.MODID);
    public static void init(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

    public static final RegistryObject<ContainerType<AgingChamberContainer>> AGING_CHAMBER_CONTAINER
            = CONTAINERS.register("aging_chamber_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new AgingChamberContainer(windowId, world, pos, inv, inv.player);
            })));

    public static final RegistryObject<ContainerType<OvenContainer>> OVEN_CONTAINER
            = CONTAINERS.register("oven_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new OvenContainer(windowId, world, pos, inv, inv.player);
            })));
}
