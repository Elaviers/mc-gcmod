package stupidmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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

        //this.passEvents = false;
        this.field_230711_n_ = false;
    }

    @Override
    public void func_230430_a_(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(matrices);
        super.func_230430_a_(matrices, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrices, mouseX, mouseY);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrices, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.f, 1.f, 1.f, 1.f);

        //this.minecraft...
        this.field_230706_i_.getTextureManager().bindTexture(new ResourceLocation("stupidmod:textures/gui/centrifuge.png"));
        int i = (this.field_230708_k_ - this.xSize) / 2;  //this.width
        int j = (this.field_230709_l_ - this.ySize) / 2; //this.height
        this.func_238474_b_(matrices, i, j, 0, 0, this.xSize, this.ySize); //this.blit

        if (this.tileent != null)
            this.func_238474_b_(matrices,i + 79,j + 33,176,0,18, (CentrifugeTileEntity.timerMax - this.tileent.getRemainingTicks()) * 19 / CentrifugeTileEntity.timerMax);  //BLIT
    }
}