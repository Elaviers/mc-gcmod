package StupidMod;

import StupidMod.Commands.CommandRareDrop;
import StupidMod.Entities.Mob.EntityPooCow;
import StupidMod.Entities.Mob.EntityPooPig;
import StupidMod.Entities.Mob.EntityPooSheep;
import StupidMod.Misc.GuiHandler;
import StupidMod.Misc.OreGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = StupidMod.id)
public class StupidMod {
    public static final String id = "stupidmod";

    @Instance(owner = id)
    public static StupidMod instance;
    
    @SidedProxy(clientSide="StupidMod.Client.ProxyClient", serverSide="StupidMod.Proxy")
    public static Proxy proxy;
    //

    public Logger logger;
    
    public ItemRegister items;
    public BlockRegister blocks;
    public EntityRegister entities;
    public RecipeRegister recipes;
    public SoundRegister sounds;
    
    public CreativeTabs creativeTab;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
        creativeTab = new CreativeTabs("stupid_tab") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(blocks.blockPoo);
            }
        };
        
        items = new ItemRegister();
        blocks = new BlockRegister();
        entities = new EntityRegister();
        recipes = new RecipeRegister();
        sounds = new SoundRegister();
        
        items.init();
        blocks.init();
        entities.init();
        recipes.init();
        sounds.init();
    
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
        this.replaceSpawnsWithPooAnimals();
        
        MinecraftForge.EVENT_BUS.register(proxy);
        proxy.registerEntityRenders();
    
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }
    
    @EventHandler
    public void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRareDrop());
    }
    
    void replaceSpawnsWithPooAnimals() {
        Biome[] spawnbiomes = {Biomes.BEACH,Biomes.BIRCH_FOREST,Biomes.BIRCH_FOREST_HILLS,Biomes.COLD_BEACH,Biomes.COLD_TAIGA,Biomes.COLD_TAIGA_HILLS,
                Biomes.EXTREME_HILLS,Biomes.EXTREME_HILLS_EDGE,Biomes.EXTREME_HILLS_WITH_TREES,Biomes.FOREST,Biomes.FOREST_HILLS,Biomes.ICE_MOUNTAINS,
                Biomes.ICE_PLAINS,Biomes.JUNGLE,Biomes.JUNGLE_EDGE,Biomes.JUNGLE_HILLS,Biomes.MESA_CLEAR_ROCK,Biomes.MESA_ROCK,Biomes.MESA,
                Biomes.PLAINS,Biomes.RIVER,Biomes.ROOFED_FOREST,Biomes.SAVANNA,Biomes.SAVANNA_PLATEAU,
                Biomes.STONE_BEACH,Biomes.SWAMPLAND,Biomes.TAIGA,Biomes.TAIGA_HILLS};
    
        EntityRegistry.removeSpawn(EntityCow.class, EnumCreatureType.CREATURE, spawnbiomes);
        EntityRegistry.addSpawn(EntityPooCow.class, 8,4,4, EnumCreatureType.CREATURE, spawnbiomes);
        EntityRegistry.removeSpawn(EntityPig.class, EnumCreatureType.CREATURE, spawnbiomes);
        EntityRegistry.addSpawn(EntityPooPig.class, 10,4,4, EnumCreatureType.CREATURE, spawnbiomes);
        EntityRegistry.removeSpawn(EntitySheep.class, EnumCreatureType.CREATURE, spawnbiomes);
        EntityRegistry.addSpawn(EntityPooSheep.class, 12,4,4, EnumCreatureType.CREATURE, spawnbiomes);
    }
}
