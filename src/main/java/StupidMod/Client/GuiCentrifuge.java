package stupidmod.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import stupidmod.entity.tile.TileEntityCentrifuge;
import stupidmod.misc.ContainerCentrifuge;

public class GuiCentrifuge extends GuiContainer {
    
    private TileEntityCentrifuge tileent;
    private IInventory inv;
    
    public GuiCentrifuge(IInventory playerInv, TileEntityCentrifuge te, boolean locked) {
        super(new ContainerCentrifuge(playerInv, te, locked));
        
        this.tileent = te;
        this.inv = playerInv;
        this.xSize = 176;
        this.ySize = 166;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("stupidmod:textures/gui/centrifuge.png"));
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i + 79,j + 33,176,0,18, (TileEntityCentrifuge.timerMax - this.tileent.getRemainingTicks()) * 19 / TileEntityCentrifuge.timerMax);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileent.getDisplayName().getFormattedText();
        this.fontRenderer.drawString(s, 88 - this.fontRenderer.getStringWidth(s) / 2, 3, 4210752);
        this.fontRenderer.drawString(this.inv.getDisplayName().getUnformattedComponentText(), 8, 72, 4210752);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
}