package StupidMod.Client;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.StupidMod;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class SoundCentrifuge extends MovingSound {
    
    public TileEntityCentrifuge entity;
    
    public SoundCentrifuge(SoundEvent soundResource, BlockPos position, TileEntityCentrifuge ent) {
        super(soundResource, SoundCategory.BLOCKS);
        this.entity = ent;
        this.repeat = true;
        this.xPosF = position.getX() + .5f;
        this.yPosF = position.getY() + .5f;
        this.zPosF = position.getZ() + .5f;
        this.volume = .01f;
    }
    
    @Override
    public void update() {
        if (entity == null || entity.isInvalid()) {
            this.donePlaying = true;
            StupidMod.proxy.removeCentrifugeSound(new BlockPos((int)(this.xPosF - 0.5f), (int)(this.yPosF - 0.5f), (int)(this.zPosF - 0.5f)));
            return;
        }
        
        if (entity.isSpinning() && volume < 1)
            volume += .02f;
        else if (!entity.isSpinning() && volume > 0)
            volume -= .02f;
        
        this.pitch = volume;
    }
    
    
}
