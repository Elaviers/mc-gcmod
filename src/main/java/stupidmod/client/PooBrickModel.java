package stupidmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PooBrickModel extends Model {
    ModelRenderer Block;
    
    public PooBrickModel()
    {
        super(RenderType::getEntitySolid);
        textureWidth = 32;
        textureHeight = 32;
        
        Block = new ModelRenderer(this, 0, 0);
        Block.addBox(-4,-2,-2,8,4,4);
        Block.setRotationPoint(0,0,0);
        Block.setTextureSize(32, 32);
        Block.mirror = true;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        Block.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }
}
