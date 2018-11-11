package StupidMod.Client.Render;

import StupidMod.Entities.EntityPoo;
import StupidMod.Client.ModelPoo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderPoo extends Render<EntityPoo> {
    ModelPoo model = new ModelPoo();
    
    public RenderPoo(RenderManager renderManager) {
        super(renderManager);
        
        this.shadowSize = 0;
    }

    @Override
    public void doRender(EntityPoo entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        
        float size = entity.prevSize + partialTicks * (entity.size - entity.prevSize);
        GlStateManager.scale(size,size,size);
        
        bindTexture(new ResourceLocation("stupidmod:textures/entity/entitypoo.png"));
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    protected ResourceLocation getEntityTexture(EntityPoo entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
