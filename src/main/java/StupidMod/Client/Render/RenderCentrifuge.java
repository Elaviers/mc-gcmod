package stupidmod.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import stupidmod.client.ModelCentrifuge;
import stupidmod.entity.tile.TileEntityCentrifuge;

public class RenderCentrifuge extends TileEntityRenderer<TileEntityCentrifuge> {
    private ModelCentrifuge model;
    
    public RenderCentrifuge()
    {
        model = new ModelCentrifuge();
    }
    
    @Override
    public void render(TileEntityCentrifuge te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y, (float)z);
        bindTexture(new ResourceLocation("stupidmod:textures/entity/entitycentrifuge.png"));
        
        //Interpolate angle
        float angle = te.prevAngle + partialTicks * (te.angle - te.prevAngle);
        //if (angle >= Math.PI * 2)
        //    angle -= Math.PI * 2;
        
        this.model.render(angle, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
