package StupidMod;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class  Proxy {
    public void registerEntityRenders() {}
    
    public void playSound(ISound sound) {}
    public ISound getSound(BlockPos pos) { return null; }
    public void stopSound(ISound sound) {}
}
