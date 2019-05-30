package stupidmod.client;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import stupidmod.entity.tile.TileEntityCentrifuge;

public class SoundCentrifuge extends MovingSound {
    
    public TileEntityCentrifuge entity;
    
    public SoundCentrifuge(SoundEvent soundResource, BlockPos position, TileEntityCentrifuge ent) {
        super(soundResource, SoundCategory.BLOCKS);
        this.entity = ent;
        this.repeat = true;
        this.x = position.getX() + .5f;
        this.y = position.getY() + .5f;
        this.z = position.getZ() + .5f;
        this.volume = .01f;
    }
    
    @Override
    public void tick() {
        if (entity == null || entity.isRemoved()) {
            this.donePlaying = true;
            //StupidMod.proxy.removeCentrifugeSound(new BlockPos((int)(this.xPosF - 0.5f), (int)(this.yPosF - 0.5f), (int)(this.zPosF - 0.5f)));
            return;
        }
        
        if (entity.isSpinning() && volume < 1)
            volume += .02f;
        else if (!entity.isSpinning() && volume > 0)
            volume -= .02f;
        
        this.pitch = volume;
    }
    
    
}
