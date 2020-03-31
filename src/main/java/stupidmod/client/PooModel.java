package stupidmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PooModel extends Model
{
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    
    public PooModel()
    {
        super(RenderType::getEntitySolid);
        textureWidth = 64;
        textureHeight = 32;
        
        Shape1 = new ModelRenderer(this, 0, 16);
        Shape1.addBox(0F, 0F, 0F, 12, 4, 12);
        Shape1.setRotationPoint(-6F, 0F, -6F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 6);
        Shape2.addBox(0F, 0F, 0F, 8, 2, 8);
        Shape2.setRotationPoint(-4F, 4F, -4F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 1);
        Shape3.addBox(0F, 0F, 0F, 4, 1, 4);
        Shape3.setRotationPoint(-2F, 6F, -2F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
    }
    
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        Shape1.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Shape2.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Shape3.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }
}
