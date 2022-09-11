package io.github.teamsidereals.foodoverflow.event.loottable;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class PigFatLootModifier extends LootModifier {
    private final Item addition;
    private final int min;
    private final int max;

    protected PigFatLootModifier(ILootCondition[] conditionsIn, Item addition, int min, int max) {
        super(conditionsIn);
        this.addition = addition;
        this.min = min;
        this.max = max;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Random roll = new Random();
        generatedLoot.add(new ItemStack(addition, roll.nextInt(max) + min));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<PigFatLootModifier> {

        @Override
        public PigFatLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            Item addition = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(JSONUtils.getAsString(object, "addition")));
            int min = JSONUtils.getAsInt(object, "min");
            int max = JSONUtils.getAsInt(object, "max");
            return new PigFatLootModifier(conditionsIn, addition, min, max);
        }

        @Override
        public JsonObject write(PigFatLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            json.addProperty("min", instance.min);
            json.addProperty("max", instance.max);
            return json;
        }
    }
}
