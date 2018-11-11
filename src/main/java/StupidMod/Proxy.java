package StupidMod;

import StupidMod.Entities.Tile.TileEntityCentrifuge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class  Proxy {

    @SubscribeEvent
    protected void registerModels(ModelRegistryEvent event) {}
    
    public void registerEntityRenders() {}
    
    public void createSoundForCentrifuge(TileEntityCentrifuge ent) {}
}
