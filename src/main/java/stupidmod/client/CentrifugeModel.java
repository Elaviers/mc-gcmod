package stupidmod.client;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CentrifugeModel extends Model
{
    ModelRenderer Base,Base2,Rod,Arm1,Arm2,Arm3,Arm4,Drum1,Drum2,Drum3,Drum4,Drum5,Drum6,Drum7,Drum8,Drum9,Drum10,Drum11,Drum12;
    
    public CentrifugeModel()
    {
        super(RenderType::getEntitySolid);
        textureWidth = 64;
        textureHeight = 32;
        
        Base = new ModelRenderer(this, 0, 18);
        Base.addBox(2,0,2, 12, 2, 12);
        Base.setTextureSize(64, 32);
        Base.mirror = true;
        Base2 = new ModelRenderer(this, 0, 9);
        Base2.addBox(4, 2, 4, 8, 1, 8);
        Base2.setTextureSize(64, 32);
        Base2.mirror = true;
        Rod = new ModelRenderer(this, 56, 17);
        Rod.addBox(-1,0,-1, 2, 13, 2);
        Rod.setRotationPoint(8, 3, 8);
        Rod.setTextureSize(64, 32);
        Rod.mirror = true;
        Arm1 = new ModelRenderer(this, 0, 0);
        Arm1.addBox(1,0,-1, 7,1,2);
        Arm1.setRotationPoint(8, 15, 8);
        Arm1.setTextureSize(64, 32);
        Arm1.mirror = true;
        Arm2 = new ModelRenderer(this, 18, 0);
        Arm2.addBox(-1,0,1, 2,1,7);
        Arm2.setRotationPoint(8, 15, 8);
        Arm2.setTextureSize(64, 32);
        Arm2.mirror = true;
        Arm3 = new ModelRenderer(this, 0, 3);
        Arm3.addBox(-8,0,-1, 7,1,2);
        Arm3.setRotationPoint(8, 15, 8);
        Arm3.setTextureSize(64, 32);
        Arm3.mirror = true;
        Arm4 = new ModelRenderer(this, 36, 0);
        Arm4.addBox(-1,0,-8, 2,1,7);
        Arm4.setRotationPoint(8, 15, 8);
        Arm4.setTextureSize(64, 32);
        Arm4.mirror = true;
        Drum1 = new ModelRenderer(this, 24, 8);
        Drum1.addBox(-2,-2,7, 4,2,1);
        Drum1.setRotationPoint(8, 15, 8);
        Drum1.setTextureSize(64, 32);
        Drum1.mirror = true;
        Drum2 = new ModelRenderer(this, 24, 11);
        Drum2.addBox(7,-2,-2, 1,2,4);
        Drum2.setRotationPoint(8, 15, 8);
        Drum2.setTextureSize(64, 32);
        Drum2.mirror = true;
        Drum3 = new ModelRenderer(this, 36, 17);
        Drum3.addBox(-2,-2,-8, 4,2,1);
        Drum3.setRotationPoint(8, 15, 8);
        Drum3.setTextureSize(64, 32);
        Drum3.mirror = true;
        Drum4 = new ModelRenderer(this, 54, 5);
        Drum4.addBox(-8,-2,-2, 1,2,4);
        Drum4.setRotationPoint(8, 15, 8);
        Drum4.setTextureSize(64, 32);
        Drum4.mirror = true;
        Drum5 = new ModelRenderer(this, 34, 8);
        Drum5.addBox(-2,-2,7, 4,2,1);
        Drum5.setRotationPoint(8, 15, 8);
        Drum5.setTextureSize(64, 32);
        Drum5.mirror = true;
        Drum6 = new ModelRenderer(this, 34, 11);
        Drum6.addBox(7,-2,-2, 1,2,4);
        Drum6.setRotationPoint(8, 15, 8);
        Drum6.setTextureSize(64, 32);
        Drum6.mirror = true;
        Drum7 = new ModelRenderer(this, 36 ,20);
        Drum7.addBox(-2,-2,-8, 4,2,1);
        Drum7.setRotationPoint(8, 15, 8);
        Drum7.setTextureSize(64, 32);
        Drum7.mirror = true;
        Drum8 = new ModelRenderer(this, 54, 11);
        Drum8.addBox(-8,-2,-2, 1,2,4);
        Drum8.setRotationPoint(8, 15, 8);
        Drum8.setTextureSize(64, 32);
        Drum8.mirror = true;
        Drum9 = new ModelRenderer(this, 44, 8);
        Drum9.addBox(-2,-2,7, 4,2,1);
        Drum9.setRotationPoint(8, 15, 8);
        Drum9.setTextureSize(64, 32);
        Drum9.mirror = true;
        Drum10 = new ModelRenderer(this, 44, 11);
        Drum10.addBox(7,-2,-2, 1,2,4);
        Drum10.setRotationPoint(8, 15, 8);
        Drum10.setTextureSize(64, 32);
        Drum10.mirror = true;
        Drum11 = new ModelRenderer(this, 36, 23);
        Drum11.addBox(-2,-2,-8, 4,2,1);
        Drum11.setRotationPoint(8, 15, 8);
        Drum11.setTextureSize(64, 32);
        Drum11.mirror = true;
        Drum12 = new ModelRenderer(this, 46, 17);
        Drum12.addBox(-8,-2,-2, 1,2,4);
        Drum12.setRotationPoint(8, 15, 8);
        Drum12.setTextureSize(64, 32);
        Drum12.mirror = true;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        Base.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Base2.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Rod.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Arm1.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Arm2.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Arm3.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Arm4.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum1.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum2.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum3.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum4.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum5.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum6.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum7.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum8.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum9.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum10.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum11.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
        Drum12.render(matrixStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    public void render(float angle, MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int i, int i1, float v, float v1, float v2, float v3)
    {
        Rod.rotateAngleY = angle;
        Arm1.rotateAngleY = angle;
        Arm2.rotateAngleY = angle;
        Arm3.rotateAngleY = angle;
        Arm4.rotateAngleY = angle;
        Drum1.rotateAngleY = Drum2.rotateAngleY = Drum3.rotateAngleY = Drum4.rotateAngleY = angle;
        Drum5.rotateAngleY = Drum6.rotateAngleY = Drum7.rotateAngleY = Drum8.rotateAngleY = angle + (float)Math.PI / 6;
        Drum9.rotateAngleY = Drum10.rotateAngleY = Drum11.rotateAngleY = Drum12.rotateAngleY = angle + (float)Math.PI / 3;
        this.render(matrixStack, iVertexBuilder, i, i1, v, v1, v2, v3);
    }
}