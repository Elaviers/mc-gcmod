package StupidMod.Client;

import StupidMod.Entities.Mob.EntityPooCow;
import StupidMod.Entities.Mob.EntityPooPig;
import StupidMod.Entities.Mob.EntityPooSheep;
import StupidMod.Proxy;
import StupidMod.Client.Render.*;
import StupidMod.Entities.*;
import StupidMod.Entities.Tile.TileEntityCentrifuge;
import StupidMod.StupidMod;
import StupidMod.Utility;
import com.sun.javafx.geom.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ProxyClient extends Proxy {
    
    @SubscribeEvent
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
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosive.class, new IRenderFactory<EntityExplosive>() {
            @Override
            public Render<? super EntityExplosive> createRenderFor(RenderManager manager) {
                return new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(1));
            }
        });
    
        RenderingRegistry.registerEntityRenderingHandler(EntityConstructiveExplosive.class, new IRenderFactory<EntityExplosive>() {
            @Override
            public Render<? super EntityExplosive> createRenderFor(RenderManager manager) {
                return new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(4));
            }
        });
    
        RenderingRegistry.registerEntityRenderingHandler(EntityImpactExplosive.class, new IRenderFactory<EntityImpactExplosive>() {
            @Override
            public Render<? super EntityImpactExplosive> createRenderFor(RenderManager manager) {
                return new RenderImpactExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(8), 0.5f);
            }
        });
        
        RenderingRegistry.registerEntityRenderingHandler(EntityAirStrikeExplosive.class, new IRenderFactory<EntityExplosive>() {
            @Override
            public Render<? super EntityExplosive> createRenderFor(RenderManager manager) {
                return new RenderExplosive(manager, StupidMod.instance.blocks.blockExplosive.getStateFromMeta(10));
            }
        });
    
        RenderingRegistry.registerEntityRenderingHandler(EntityPoo.class, new IRenderFactory<EntityPoo>() {
            @Override
            public Render<? super EntityPoo> createRenderFor(RenderManager manager) {
                return new RenderPoo(manager);
            }
        });
        
        RenderingRegistry.registerEntityRenderingHandler(EntityPooBrick.class, new IRenderFactory<EntityPooBrick>() {
            @Override
            public Render<? super EntityPooBrick> createRenderFor(RenderManager manager) {
                return new RenderPooBrick(manager);
            }
        });
    
        RenderingRegistry.registerEntityRenderingHandler(EntityPooExplosive.class, new IRenderFactory<EntityPooExplosive>() {
            @Override
            public Render<? super EntityPooExplosive> createRenderFor(RenderManager manager) {
                return new RenderPooExplosive(manager);
            }
        });
        
        RenderingRegistry.registerEntityRenderingHandler(EntityPooCow.class, new IRenderFactory<EntityPooCow>() {
            @Override
            public Render<? super EntityPooCow> createRenderFor(RenderManager manager) {
                return new RenderCow(manager);
            }
        });
    
        RenderingRegistry.registerEntityRenderingHandler(EntityPooPig.class, new IRenderFactory<EntityPooPig>() {
            @Override
            public Render<? super EntityPooPig> createRenderFor(RenderManager manager) {
                return new RenderPig(manager);
            }
        });
    
        RenderingRegistry.registerEntityRenderingHandler(EntityPooSheep.class, new IRenderFactory<EntityPooSheep>() {
            @Override
            public Render<? super EntityPooSheep> createRenderFor(RenderManager manager) {
                return new RenderSheep(manager);
            }
        });
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCentrifuge.class, new RenderCentrifuge());
    }
    
    Map<Vec3f, ISound> playingSounds = new HashMap<>();
    
    @SubscribeEvent
    public void worldUnLoaded(WorldEvent.Unload event) {
        playingSounds.clear();
    }
    
    @Override
    public void playSound(ISound sound) {
        Vec3f soundPos = new Vec3f(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
        playingSounds.put(soundPos, sound);
        
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
    }
    
    @Override
    public ISound getSound(BlockPos pos) {
        Vec3f soundPos = new Vec3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        return playingSounds.get(soundPos);
    }
    
    @Override
    public void stopSound(ISound sound) {
        Vec3f soundPos = new Vec3f(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
        playingSounds.remove(soundPos);
        
        Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
    }
}
