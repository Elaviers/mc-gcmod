package stupidmod.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import stupidmod.entity.EntityImpactExplosive;

import javax.annotation.Nullable;

public class RenderImpactExplosive extends Render<EntityImpactExplosive> {
    final float size;
    final IBlockState rState;
    
    public RenderImpactExplosive(RenderManager renderManager, IBlockState state, float size) {
        super(renderManager);
        
        this.size = size;
        this.rState = state;
    }
    
    @Override
    public void doRender(EntityImpactExplosive entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + 0.5F, (float)z);
    
        GlStateManager.rotatef(entity.prevAngleX + partialTicks * (entity.angleX - entity.prevAngleX), 1,0,0);
        GlStateManager.rotatef(entity.prevAngleY + partialTicks * (entity.angleY - entity.prevAngleY), 0,1,0);
        GlStateManager.rotatef(entity.prevAngleZ + partialTicks * (entity.angleZ - entity.prevAngleZ), 0,0,1);
    
        GlStateManager.scalef(size, size, size);
    
        this.bindEntityTexture(entity);
        GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(rState, entity.getBrightness());
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
    
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityImpactExplosive entity) {
        return null;
    }
}
