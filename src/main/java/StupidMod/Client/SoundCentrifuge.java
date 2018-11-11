package StupidMod.Client;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.StupidMod;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class SoundCentrifuge extends MovingSound {
    
    public TileEntityCentrifuge c;
    
    public SoundCentrifuge(SoundEvent soundResource, BlockPos position, TileEntityCentrifuge ent) {
        super(soundResource, SoundCategory.BLOCKS);
        this.c = ent;
        this.repeat = true;
        this.xPosF = position.getX() + .5f;
        this.yPosF = position.getY() + .5f;
        this.zPosF = position.getZ() + .5f;
        this.volume = .01f;
    }
    
    @Override
    public void update() {
        
        if (c.isInvalid()) {
            this.donePlaying = true;
            return;
        }
        
        if (c.isSpinning() && volume < 1)
            volume += .02f;
        else if (!c.isSpinning() && volume > 0)
            volume -= .02f;
        
        this.pitch = volume;
    }
}
