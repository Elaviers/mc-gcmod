package stupidmod.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.client.PooBrickModel;
import stupidmod.entity.PooBrickEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class PooBrickEntityRenderer extends EntityRenderer<PooBrickEntity> {
    private final ResourceLocation TEXTURE = new ResourceLocation(StupidMod.id, "textures/item/poo_brick.png");

    PooBrickModel model = new PooBrickModel();

    public PooBrickEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(PooBrickEntity entity) { return TEXTURE; }

    @Override
    public void doRender(PooBrickEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + .125f, (float)z);

        GlStateManager.rotatef(entity.prevAngleX + partialTicks * (entity.angleX - entity.prevAngleX), 1,0,0);
        GlStateManager.rotatef(entity.prevAngleY + partialTicks * (entity.angleY - entity.prevAngleY), 0,1,0);
        GlStateManager.rotatef(entity.prevAngleZ + partialTicks * (entity.angleZ - entity.prevAngleZ), 0,0,1);

        this.bindEntityTexture(entity);
        this.model.render();

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
