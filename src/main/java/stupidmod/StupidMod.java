package stupidmod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DesertBiome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.CompositeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import stupidmod.misc.GuiHandler;

@Mod(StupidMod.id)
public class StupidMod {
    public static final String id = "stupidmod";
    
    public StupidMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
    }
    
    
    
    private void setup(FMLCommonSetupEvent event)
    {
        RecipeRegister.registerRecipes();

        //Gen
        CompositeFeature sulphurFeature = Biome.createCompositeFeature(
                Feature.MINABLE,
                new MinableConfig(
                        MinableConfig.IS_ROCK,
                        BlockRegister.blockSulphurOre.getDefaultState(),
                        20),
                Biome.COUNT_RANGE,
                new CountRangeConfig(
                        12,
                        0,
                        0,
                        48
                )
        );
    
        CompositeFeature noahSulphurFeature = Biome.createCompositeFeature(
                Feature.MINABLE,
                new MinableConfig(
                        MinableConfig.IS_ROCK,
                        BlockRegister.blockNoahSulphurOre.getDefaultState(),
                        5),
                Biome.COUNT_RANGE,
                new CountRangeConfig(
                        5,
                        0,
                        0,
                        20
                )
        );
    
        Utility.addOverworldOreFeature(GenerationStage.Decoration.UNDERGROUND_ORES, sulphurFeature);
        Utility.addOverworldOreFeature(GenerationStage.Decoration.UNDERGROUND_ORES, noahSulphurFeature);
    
        Utility.removeSpawn(EntityType.COW);
        Utility.addOverworldCreatureSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityRegister.entityPooCow, 8, 4, 4));
    
        Utility.removeSpawn(EntityType.PIG);
        Utility.addOverworldCreatureSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityRegister.entityPooPig, 10, 4, 4));
    
        Utility.removeSpawn(EntityType.SHEEP);
        Utility.addOverworldCreatureSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityRegister.entityPooSheep, 12, 4, 4));
        
    }
    
    private void clientSetup(FMLClientSetupEvent event)
    {
        EntityRegister.registerRenderers();
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event)
    {
    
    }
    
    public static final ItemGroup GROUP = new ItemGroup("stupidmod") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BlockRegister.blockPoo);
        }
    };
    
}
