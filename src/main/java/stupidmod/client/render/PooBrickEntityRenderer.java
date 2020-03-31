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
import stupidmod.entity.PooBrickEntity;

@OnlyIn(Dist.CLIENT)
public class PooBrickEntityRenderer extends EntityRenderer<PooBrickEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(StupidMod.id, "textures/item/poo_brick.png");

    PooBrickModel model = new PooBrickModel();

    public PooBrickEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(PooBrickEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(PooBrickEntity pooBrick, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer types, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0.0, 0.125, 0.0);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(pooBrick.prevAngleX + partialTicks * (pooBrick.angleX - pooBrick.prevAngleX)));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(pooBrick.prevAngleY + partialTicks * (pooBrick.angleY - pooBrick.prevAngleY)));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(pooBrick.prevAngleZ + partialTicks * (pooBrick.angleZ - pooBrick.prevAngleZ)));

        this.model.render(matrixStack, types.getBuffer(this.model.getRenderType(this.getEntityTexture(pooBrick))), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        matrixStack.pop();

        super.render(pooBrick, entityYaw, partialTicks, matrixStack, types, packedLight);
    }
}
