package stupidmod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public ResourceLocation getEntityTexture(ImpactExplosiveEntity entity) { return AtlasTexture.LOCATION_BLOCKS_TEXTURE; }

    @Override
    public void render(ImpactExplosiveEntity explosive, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer types, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0.0, 0.5, 0.0);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(explosive.prevAngleX + partialTicks * (explosive.angleX - explosive.prevAngleX)));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(explosive.prevAngleY + partialTicks * (explosive.angleY - explosive.prevAngleY)));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(explosive.prevAngleZ + partialTicks * (explosive.angleZ - explosive.prevAngleZ)));

        matrixStack.scale(size, size, size);

        matrixStack.translate(-0.5F, -0.5F, 0.5F);
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(rState, matrixStack, types, packedLight, OverlayTexture.NO_OVERLAY);
        matrixStack.translate(0.0F, 0.0F, 1.0F);

        matrixStack.pop();

        super.render(explosive, entityYaw, partialTicks, matrixStack, types, packedLight);
    }
}
