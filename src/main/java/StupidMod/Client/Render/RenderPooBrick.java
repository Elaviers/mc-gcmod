package StupidMod.Client.Render;

import StupidMod.Entities.EntityPooBrick;
import StupidMod.Client.ModelPooBrick;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderPooBrick extends Render<EntityPooBrick> {
    ModelPooBrick model = new ModelPooBrick();
    
    public RenderPooBrick(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 0;
    }
    
    public void doRender(EntityPooBrick entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
    
        GlStateManager.rotate(entity.prevAngleX + partialTicks * (entity.angleX - entity.prevAngleX), 1,0,0);
        GlStateManager.rotate(entity.prevAngleY + partialTicks * (entity.angleY - entity.prevAngleY), 0,1,0);
        GlStateManager.rotate(entity.prevAngleZ + partialTicks * (entity.angleZ - entity.prevAngleZ), 0,0,1);
        
        bindTexture(new ResourceLocation("stupidmod:textures/items/poo_brick.png"));
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    protected ResourceLocation getEntityTexture(EntityPooBrick entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

}
