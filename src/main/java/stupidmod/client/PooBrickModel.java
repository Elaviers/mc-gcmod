package stupidmod.client;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PooBrickModel extends Model {
    RendererModel Block;
    
    public PooBrickModel()
    {
        textureWidth = 32;
        textureHeight = 32;
        
        Block = new RendererModel(this, 0, 0);
        Block.addBox(-4,-2,-2,8,4,4);
        Block.setRotationPoint(0,0,0);
        Block.setTextureSize(32, 32);
        Block.mirror = true;
    }
    
    public void render()
    {
        Block.render(1/16f);
    }
    
}
