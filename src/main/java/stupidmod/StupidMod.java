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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import stupidmod.client.ClientProxy;

import java.util.List;

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
        ConfiguredFeature sulphurFeature = Feature.ORE.withConfiguration(
                new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        StupidModBlocks.SULPHUR_ORE.getDefaultState(),
                        32)
        ).withPlacement(Placement.COUNT_RANGE.configure(
                new CountRangeConfig(
                        1,
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

        BiomeDictionary.getBiomes(BiomeDictionary.Type.OVERWORLD).forEach(biome ->
            {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, sulphurFeature);
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, noahSulphurFeature);

                List<Biome.SpawnListEntry> spawns = biome.getSpawns(EntityClassification.CREATURE);

                for (int i = 0; i < spawns.size();)
                {
                    Biome.SpawnListEntry entry = spawns.get(i);

                    if (entry.entityType == EntityType.COW)
                        spawns.add(new Biome.SpawnListEntry(StupidModEntities.POO_COW, entry.itemWeight, entry.minGroupCount, entry.maxGroupCount));
                    else if (entry.entityType == EntityType.PIG)
                        spawns.add(new Biome.SpawnListEntry(StupidModEntities.POO_PIG, entry.itemWeight, entry.minGroupCount, entry.maxGroupCount));
                    else if (entry.entityType == EntityType.SHEEP)
                        spawns.add(new Biome.SpawnListEntry(StupidModEntities.POO_SHEEP, entry.itemWeight, entry.minGroupCount, entry.maxGroupCount));
                    else {
                        i++;
                        continue;
                    }

                    spawns.remove(i);
                }
            }
        );
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
