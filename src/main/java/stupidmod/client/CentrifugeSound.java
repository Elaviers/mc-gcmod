package stupidmod.client;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stupidmod.StupidMod;
import stupidmod.entity.tile.CentrifugeTileEntity;

@OnlyIn(Dist.CLIENT)
public class CentrifugeSound extends TickableSound {
    
    public CentrifugeTileEntity entity;
    
    public CentrifugeSound(SoundEvent soundResource, CentrifugeTileEntity te) {
        super(soundResource, SoundCategory.BLOCKS);
        this.entity = te;
        this.repeat = true;

        BlockPos pos = te.getPos();

        this.x = pos.getX() + .5f;
        this.y = pos.getY() + .5f;
        this.z = pos.getZ() + .5f;
        this.volume = .01f;
    }
    
    @Override
    public void tick() {
        if (entity == null || entity.isRemoved()) {
            this.func_239509_o_(); //Stops sound from playing
            StupidMod.proxy.clRemoveCentrifugeSound(new BlockPos((int)(this.x - 0.5f), (int)(this.y - 0.5f), (int)(this.z - 0.5f)));
            return;
        }
        
        if (entity.getRotationRateTarget() != 0 && volume < 1)
            volume += .02f;
        else if (entity.getRotationRateTarget() == 0 && volume > 0)
            volume -= .02f;
        
        this.pitch = volume;
    }
    
    
}
