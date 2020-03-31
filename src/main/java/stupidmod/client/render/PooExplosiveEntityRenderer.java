package stupidmod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.client.PooBrickModel;
import stupidmod.entity.PooExplosiveEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class PooExplosiveEntityRenderer extends EntityRenderer<PooExplosiveEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(StupidMod.id, "textures/item/poo_brick.png");

    PooBrickModel model = new PooBrickModel();

    public PooExplosiveEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(PooExplosiveEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(PooExplosiveEntity pooExplosive, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer types, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0.0, 0.125, 0.0);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(pooExplosive.prevAngleX + partialTicks * (pooExplosive.angleX - pooExplosive.prevAngleX)));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(pooExplosive.prevAngleY + partialTicks * (pooExplosive.angleY - pooExplosive.prevAngleY)));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(pooExplosive.prevAngleZ + partialTicks * (pooExplosive.angleZ - pooExplosive.prevAngleZ)));

        this.model.render(matrixStack, types.getBuffer(this.model.getRenderType(this.getEntityTexture(pooExplosive))), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStack.pop();
        super.render(pooExplosive, entityYaw, partialTicks, matrixStack, types, packedLight);
    }
}
