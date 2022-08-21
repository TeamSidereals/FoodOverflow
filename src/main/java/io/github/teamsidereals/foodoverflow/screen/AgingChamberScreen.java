package io.github.teamsidereals.foodoverflow.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.teamsidereals.foodoverflow.FoodOverflowMod;
import io.github.teamsidereals.foodoverflow.container.AgingChamberContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AgingChamberScreen extends ContainerScreen<AgingChamberContainer> {
    private final ResourceLocation GUI = new ResourceLocation(FoodOverflowMod.MODID,
            "textures/gui/aging_chamber_gui.png");

    public AgingChamberScreen(AgingChamberContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageHeight = 174;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    private int[] progressTracker = {0,0,0,0};

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.getXSize(), this.getYSize());

//        if(menu.isLightningStorm()) {
//            this.blit(matrixStack, i + 82, j + 9, 176, 0, 13, 10);
//        }

        for (int slot = 0; slot < 4; slot++){
            progressTracker[slot] = this.menu.getAgingProgress(slot);
            this.blit(matrixStack, i + 26 + (slot * 36), j + 36, 177, 0, 17, progressTracker[slot]);
        }
    }
}
