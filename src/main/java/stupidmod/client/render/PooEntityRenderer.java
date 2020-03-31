package stupidmod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.client.PooModel;
import stupidmod.entity.PooEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class PooEntityRenderer extends EntityRenderer<PooEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(StupidMod.id, "textures/entity/poo.png");
    private static final PooModel model = new PooModel();
    
    public PooEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        
        this.shadowSize = 0;
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(PooEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(PooEntity poo, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer types, int packedLight) {
        matrixStack.push();

        float size = poo.prevSize + partialTicks * (poo.size - poo.prevSize);
        if (size > 0) {
            matrixStack.scale(size, size, size);

            model.render(matrixStack, types.getBuffer(model.getRenderType(this.getEntityTexture(poo))), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }

        matrixStack.pop();

        super.render(poo, entityYaw, partialTicks, matrixStack, types, packedLight);
    }
}
