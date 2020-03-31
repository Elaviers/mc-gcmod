package stupidmod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.client.CentrifugeModel;
import stupidmod.entity.tile.CentrifugeTileEntity;

@OnlyIn(Dist.CLIENT)
public class CentrifugeTileEntityRenderer extends TileEntityRenderer<CentrifugeTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(StupidMod.id, "textures/entity/centrifuge.png");

    private CentrifugeModel model;
    
    public CentrifugeTileEntityRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
        model = new CentrifugeModel();
    }

    @Override
    public void render(CentrifugeTileEntity centrifuge, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer types, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        float angle = centrifuge.prevAngle + partialTicks * (centrifuge.angle - centrifuge.prevAngle);

        this.model.render(angle, matrixStack, types.getBuffer(this.model.getRenderType(TEXTURE)), combinedLight, combinedOverlay, 1, 1, 1, 1);
        matrixStack.pop();
    }
}
