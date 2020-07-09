package stupidmod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.entity.ExplosiveEntity;

@OnlyIn(Dist.CLIENT)
public class ExplosiveEntityRenderer extends EntityRenderer<ExplosiveEntity> {
    final BlockState renderState;
    
    public ExplosiveEntityRenderer(EntityRendererManager renderManager, BlockState renderState) {
        super(renderManager);
        
        this.shadowSize = 0.5f;
        
        this.renderState = renderState;
    }

    @Override
    public ResourceLocation getEntityTexture(ExplosiveEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(ExplosiveEntity explosive, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer types, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        if ((float)explosive.getFuse() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)explosive.getFuse() - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            matrixStack.scale(f1, f1, f1);
        }

        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5, -0.5, 0.5);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderTntFlash(renderState, matrixStack, types, packedLight, explosive.getFuse() / 5 % 2 == 0);
        matrixStack.pop();

        super.render(explosive, entityYaw, partialTicks, matrixStack, types, packedLight);
    }
}
