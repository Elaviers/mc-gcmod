package StupidMod;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class  Proxy {
    @SubscribeEvent
    protected  void registerModels(ModelRegistryEvent event) {}
    
    public void registerEntityRenders() {}
    
    //Adds and plays a new centrifuge sound or binds the existing one
    public void updateCentrifugeSound(TileEntityCentrifuge te) {}
    
    //Removes the sound at given position
    public void removeCentrifugeSound(BlockPos pos) { }
}
