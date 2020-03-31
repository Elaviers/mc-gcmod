package stupidmod.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.client.CentrifugeModel;
import stupidmod.entity.tile.CentrifugeTileEntity;

@OnlyIn(Dist.CLIENT)
public class CentrifugeTileEntityRenderer extends TileEntityRenderer<CentrifugeTileEntity> {
    private CentrifugeModel model;
    
    public CentrifugeTileEntityRenderer()
    {
        model = new CentrifugeModel();
    }
    
    @Override
    public void render(CentrifugeTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();

        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y, (float)z);
        bindTexture(new ResourceLocation("stupidmod:textures/entity/centrifuge.png"));
        
        //Interpolate angle
        float angle = te.prevAngle + partialTicks * (te.angle - te.prevAngle);
        //if (angle >= Math.PI * 2)
        //    angle -= Math.PI * 2;
        
        this.model.render(angle);
        GlStateManager.popMatrix();
    }
}
