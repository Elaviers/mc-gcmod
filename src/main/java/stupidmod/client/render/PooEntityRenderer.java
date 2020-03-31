package stupidmod.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.client.PooModel;
import stupidmod.entity.PooEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class PooEntityRenderer extends EntityRenderer<PooEntity> {
    private final ResourceLocation TEXTURE = new ResourceLocation(StupidMod.id, "textures/entity/poo.png");

    private static final PooModel model = new PooModel();
    
    public PooEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        
        this.shadowSize = 0;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(PooEntity entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(PooEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y, (float)z);

        float size = entity.prevSize + partialTicks * (entity.size - entity.prevSize);
        if (size <= 0)
            return;

        GlStateManager.scalef(size,size,size);

        this.bindEntityTexture(entity);
        model.render();

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
