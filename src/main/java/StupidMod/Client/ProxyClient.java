package StupidMod.Client;

import StupidMod.Client.Render.*;
import StupidMod.Entities.*;
import StupidMod.Entities.Mob.EntityPooCow;
import StupidMod.Entities.Mob.EntityPooPig;
import StupidMod.Entities.Mob.EntityPooSheep;
import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.Proxy;
import StupidMod.StupidMod;
import StupidMod.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ProxyClient extends Proxy {
    
    @Override
    protected  void registerModels(ModelRegistryEvent event) {
        StupidMod inst = StupidMod.instance;
        
        //Tnt
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 0, "tier=0,type=blast");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 1, "tier=1,type=blast");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 2, "tier=2,type=blast");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 3, "tier=0,type=construct");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 4, "tier=1,type=construct");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 5, "tier=2,type=construct");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 6, "tier=0,type=dig");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 7, "tier=1,type=dig");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 8, "tier=2,type=dig");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 9, "tier=0,type=airstrike");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 10, "tier=1,type=airstrike");
        Utility.registerRendererForItem(inst.blocks.itemBlockExplosive, 11, "tier=2,type=airstrike");
        
        //Blocks
        Utility.registerRendererForItem(inst.blocks.itemBlockPoo, 0, "fermented=false");
        Utility.registerRendererForItem(inst.blocks.itemBlockPoo, 1, "fermented=true");
        Utility.registerRendererForItem(inst.blocks.itemBlockRope, 0, "inventory");
        Utility.registerRendererForItem(Item.getItemFromBlock(inst.blocks.blockSulphurOre));
        Utility.registerRendererForItem(Item.getItemFromBlock(inst.blocks.blockNoahSulphurOre));
        Utility.registerRendererForItem(Item.getItemFromBlock(inst.blocks.blockMeme));
        Utility.registerRendererForItem(Item.getItemFromBlock(inst.blocks.blockCentrifuge));
        Utility.registerRendererForItem(Item.getItemFromBlock(inst.blocks.blockWirelessTorch), 0, "inventory");
        
        //Items
        ModelLoader.setCustomModelResourceLocation(inst.items.itemPoo, 0, new ModelResourceLocation(StupidMod.id+":poo"));
        ModelLoader.setCustomModelResourceLocation(inst.items.itemPoo, 1, new ModelResourceLocation(StupidMod.id+":poo_fermented"));
        ModelLoader.setCustomModelResourceLocation(inst.items.itemPoo, 2, new ModelResourceLocation(StupidMod.id+":poo"));
    
        Utility.registerRendererForItem(inst.items.itemSulphur);
        Utility.registerRendererForItem(inst.items.itemNoahSulphur);
        Utility.registerRendererForItem(inst.items.itemMemeEssence);
        Utility.registerRendererForItem(inst.items.itemPowder);
        Utility.registerRendererForItem(inst.items.itemPooPowder);
        Utility.registerRendererForItem(inst.items.itemPooProtein);
        Utility.registerRendererForItem(inst.items.itemPooBrick);
        
        ModelLoader.setCustomModelResourceLocation(inst.items.itemPooCannon, 0, new ModelResourceLocation("stupidmod:poo_cannon"));
        ModelLoader.setCustomModelResourceLocation(inst.items.itemPooCannon, 1, new ModelResourceLocation("stupidmod:poo_cannon_active"));
        
        Utility.registerRendererForItem(inst.items.itemCalibrator);
    }
    
    @Override
    public void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosive.class, manager -> new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(1)));
        RenderingRegistry.registerEntityRenderingHandler(EntityConstructiveExplosive.class, manager -> new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(4)));
        RenderingRegistry.registerEntityRenderingHandler(EntityDigExplosive.class, manager -> new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(7)));
        RenderingRegistry.registerEntityRenderingHandler(EntityAirStrikeExplosive.class, (IRenderFactory<EntityExplosive>) manager -> new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(10)));
        RenderingRegistry.registerEntityRenderingHandler(EntityImpactExplosive.class, manager -> new RenderImpactExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(8), 0.5f));
        RenderingRegistry.registerEntityRenderingHandler(EntityPoo.class, manager -> new RenderPoo(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPooBrick.class, manager -> new RenderPooBrick(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPooExplosive.class, manager -> new RenderPooExplosive(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPooCow.class, manager -> new RenderCow(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPooPig.class, manager -> new RenderPig(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPooSheep.class, manager -> new RenderSheep(manager));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCentrifuge.class, new RenderCentrifuge());
    }
    
    Map<BlockPos, SoundCentrifuge> playingSounds = new HashMap<>();
    
    @SubscribeEvent
    public void worldUnLoaded(WorldEvent.Unload event) {
        playingSounds.clear();
    }
    
    //Adds and plays a new centrifuge sound or binds the existing one
    public void updateCentrifugeSound(TileEntityCentrifuge te) {
        BlockPos pos = te.getPos();
        
        SoundCentrifuge existing = playingSounds.get(pos);
        if (existing != null) {
            existing.entity = te;
        }
        else {
            existing = new SoundCentrifuge(StupidMod.instance.sounds.soundCentrifuge, te.getPos(), te);
            Minecraft.getMinecraft().getSoundHandler().playSound(existing);
            playingSounds.put(pos, existing);
        }
    }
    
    //Removes the sound at given position
    public void removeCentrifugeSound(BlockPos pos) {
        playingSounds.remove(pos);
    }
}
