package StupidMod.Client.Render;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.Client.ModelCentrifuge;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderCentrifuge extends TileEntitySpecialRenderer<TileEntityCentrifuge> {
    private ModelCentrifuge model;
    
    public RenderCentrifuge()
    {
        model = new ModelCentrifuge();
    }
    
    @Override
    public void render(TileEntityCentrifuge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        bindTexture(new ResourceLocation("stupidmod:textures/entity/entitycentrifuge.png"));
        
        //Interpolate angle
        float angle = te.prevAngle + partialTicks * (te.angle - te.prevAngle);
        //if (angle >= Math.PI * 2)
        //    angle -= Math.PI * 2;
        
        this.model.render(angle, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
