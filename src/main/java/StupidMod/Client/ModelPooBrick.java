package stupidmod.client;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPooBrick extends ModelBase {
    ModelRenderer Block;
    
    public ModelPooBrick()
    {
        textureWidth = 32;
        textureHeight = 32;
        
        Block = new ModelRenderer(this, 0, 0);
        Block.addBox(-4,-2,-2,8,4,4);
        Block.setRotationPoint(0,0,0);
        Block.setTextureSize(32, 32);
        Block.mirror = true;
    }
    
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5,entity);
        Block.render(f5);
    }
    
}
