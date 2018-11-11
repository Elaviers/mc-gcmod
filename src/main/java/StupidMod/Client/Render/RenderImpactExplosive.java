package StupidMod.Client.Render;

import StupidMod.Entities.EntityImpactExplosive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderImpactExplosive extends Render<EntityImpactExplosive>
{
    float Size;
    IBlockState rState;
    
    public RenderImpactExplosive(RenderManager manager, IBlockState RenderState, float size)
    {
        super(manager);
        rState = RenderState;
        Size = size;
        this.shadowSize = 0.5F;
    }
    
    public void doRender(EntityImpactExplosive entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);
        
        GlStateManager.rotate(entity.prevAngleX + partialTicks * (entity.angleX - entity.prevAngleX), 1,0,0);
        GlStateManager.rotate(entity.prevAngleY + partialTicks * (entity.angleY - entity.prevAngleY), 0,1,0);
        GlStateManager.rotate(entity.prevAngleZ + partialTicks * (entity.angleZ - entity.prevAngleZ), 0,0,1);
        
        GlStateManager.scale(Size,Size,Size);
        
        this.bindEntityTexture(entity);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(rState, entity.getBrightness());
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    protected ResourceLocation getEntityTexture(EntityImpactExplosive entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}