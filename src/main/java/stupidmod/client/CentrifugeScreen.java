package stupidmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.entity.tile.CentrifugeTileEntity;
import stupidmod.misc.CentrifugeContainer;

@OnlyIn(Dist.CLIENT)
public class CentrifugeScreen extends ContainerScreen<CentrifugeContainer> implements IHasContainer<CentrifugeContainer> {
    private static final ResourceLocation CENTRIFUGE_GUI = new ResourceLocation("textures/gui/container/brewing_stand.png");

    private CentrifugeTileEntity tileent;
    private IInventory inv;
    
    public CentrifugeScreen(CentrifugeContainer container, PlayerInventory playerInv, ITextComponent text) {
        super(container, playerInv, text);

        this.tileent = container.ent;
        this.inv = playerInv;
        this.xSize = 176;
        this.ySize = 166;

        this.passEvents = false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.f, 1.f, 1.f, 1.f);

        this.minecraft.getTextureManager().bindTexture(new ResourceLocation("stupidmod:textures/gui/centrifuge.png"));
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        if (this.tileent != null)
            this.blit(matrixStack,i + 79,j + 33,176,0,18, (CentrifugeTileEntity.timerMax - this.tileent.getRemainingTicks()) * 19 / CentrifugeTileEntity.timerMax);
    }
}