package stupidmod.client;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PooModel extends Model
{
    RendererModel Shape1;
    RendererModel Shape2;
    RendererModel Shape3;
    
    public PooModel()
    {
        textureWidth = 64;
        textureHeight = 32;
        
        Shape1 = new RendererModel(this, 0, 16);
        Shape1.addBox(0F, 0F, 0F, 12, 4, 12);
        Shape1.setRotationPoint(-6F, 0F, -6F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new RendererModel(this, 0, 6);
        Shape2.addBox(0F, 0F, 0F, 8, 2, 8);
        Shape2.setRotationPoint(-4F, 4F, -4F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new RendererModel(this, 0, 1);
        Shape3.addBox(0F, 0F, 0F, 4, 1, 4);
        Shape3.setRotationPoint(-2F, 6F, -2F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
    }

    public void render()
    {
        Shape1.render(1/16f);
        Shape2.render(1/16f);
        Shape3.render(1/16f);
    }
    
    private void setRotation(RendererModel model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
