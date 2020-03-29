package stupidmod.client;

import com.mojang.blaze3d.platform.GlStateManager;
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation("stupidmod:textures/gui/centrifuge.png"));
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        if (this.tileent != null)
            this.blit(i + 79,j + 33,176,0,18, (CentrifugeTileEntity.timerMax - this.tileent.getRemainingTicks()) * 19 / CentrifugeTileEntity.timerMax);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.title.getFormattedText();
        this.font.drawString(s, 8.f, 6.f, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getUnformattedComponentText(), 8.f, 72.f, 4210752);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
}