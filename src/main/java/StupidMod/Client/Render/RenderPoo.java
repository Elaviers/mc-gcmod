package stupidmod.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import stupidmod.client.ModelPoo;
import stupidmod.entity.EntityPoo;

import javax.annotation.Nullable;

public class RenderPoo extends Render<EntityPoo> {
    private static final ModelPoo model = new ModelPoo();
    
    public RenderPoo(RenderManager renderManager) {
        super(renderManager);
        
        this.shadowSize = 0;
    }
    
    @Override
    public void doRender(EntityPoo entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y, (float)z);
    
        float size = entity.prevSize + partialTicks * (entity.size - entity.prevSize);
        GlStateManager.scalef(size,size,size);
    
        bindTexture(new ResourceLocation("stupidmod:textures/entity/entitypoo.png"));
        model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPoo entity) {
        return null;
    }
}
