package stupidmod;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import stupidmod.client.ClientProxy;

@Mod(StupidMod.id)
public class StupidMod {
    public static final String id = "stupidmod";

    public static Proxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> Proxy::new);
    
    public StupidMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }
    
    private void setup(FMLCommonSetupEvent event)
    {
        //Gen
        ConfiguredFeature sulphurFeature = Feature.ORE.withConfiguration(
                new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        StupidModBlocks.SULPHUR_ORE.getDefaultState(),
                        20)
        ).withPlacement(Placement.COUNT_RANGE.configure(
                new CountRangeConfig(
                        12,
                        0,
                        0,
                        48)
        ));

        ConfiguredFeature noahSulphurFeature = Feature.ORE.withConfiguration(
                new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        StupidModBlocks.NOAH_SULPHUR_ORE.getDefaultState(),
                        5)
        ).withPlacement(Placement.COUNT_RANGE.configure(
                new CountRangeConfig(
                        5,
                        0,
                        0,
                        20)
        ));


        Utility.addOverworldOreFeature(GenerationStage.Decoration.UNDERGROUND_ORES, sulphurFeature);
        Utility.addOverworldOreFeature(GenerationStage.Decoration.UNDERGROUND_ORES, noahSulphurFeature);
    
        Utility.removeSpawn(EntityType.COW);
        Utility.addOverworldCreatureSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(StupidModEntities.POO_COW, 8, 4, 4));
    
        Utility.removeSpawn(EntityType.PIG);
        Utility.addOverworldCreatureSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(StupidModEntities.POO_PIG, 10, 4, 4));
    
        Utility.removeSpawn(EntityType.SHEEP);
        Utility.addOverworldCreatureSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(StupidModEntities.POO_SHEEP, 12, 4, 4));
        
    }
    
    private void clientSetup(FMLClientSetupEvent event)
    {
        StupidModBlocks.registerRenderLayers();
        StupidModEntities.registerRenderers();
        StupidModContainers.RegisterScreenFactories();
    }
    
    public static final ItemGroup GROUP = new ItemGroup("stupidmod") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(StupidModBlocks.POO);
        }
    };
    
}
