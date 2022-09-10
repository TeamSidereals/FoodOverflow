package io.github.teamsidereals.foodoverflow.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.container.OvenContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OvenScreen extends ContainerScreen<OvenContainer> {
    private final ResourceLocation GUI = new ResourceLocation(FoodOverflowMod.MODID,
            "textures/gui/oven_gui.png");

    public OvenScreen(OvenContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.getXSize(), this.getYSize());

        int progress = this.menu.getCookingProgress();
        this.blit(matrixStack, i + 84, j + 32, 177, 0, progress, 17);
    }
}
