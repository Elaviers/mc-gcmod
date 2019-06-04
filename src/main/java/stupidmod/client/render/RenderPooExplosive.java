package stupidmod.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.client.ModelPooBrick;
import stupidmod.entity.EntityPooExplosive;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderPooExplosive extends Render<EntityPooExplosive> {
    ModelPooBrick model = new ModelPooBrick();

    public RenderPooExplosive(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPooExplosive entity) {
        return null;
    }

    @Override
    public void doRender(EntityPooExplosive entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + .125f, (float)z);

        GlStateManager.rotatef(entity.prevAngleX + partialTicks * (entity.angleX - entity.prevAngleX), 1,0,0);
        GlStateManager.rotatef(entity.prevAngleY + partialTicks * (entity.angleY - entity.prevAngleY), 0,1,0);
        GlStateManager.rotatef(entity.prevAngleZ + partialTicks * (entity.angleZ - entity.prevAngleZ), 0,0,1);

        bindTexture(new ResourceLocation("stupidmod:textures/item/poo_brick.png"));
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
