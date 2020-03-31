package stupidmod.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.entity.ImpactExplosiveEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ImpactExplosiveEntityRenderer extends EntityRenderer<ImpactExplosiveEntity> {
    final float size;
    final BlockState rState;

    public ImpactExplosiveEntityRenderer(EntityRendererManager renderManager, BlockState state, float size) {
        super(renderManager);
        
        this.size = size;
        this.rState = state;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ImpactExplosiveEntity entity) { return AtlasTexture.LOCATION_BLOCKS_TEXTURE; }

    @Override
    public void doRender(ImpactExplosiveEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
}
