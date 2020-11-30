package stupidmod;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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

    private ConfiguredFeature sulphurFeature;
    private ConfiguredFeature noahSulphurFeature;

    public StupidMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeSetup);
    }

    private void setup(FMLCommonSetupEvent setup)
    {
        //func_242731_b = Features per Chunk!

        sulphurFeature = Feature.ORE.withConfiguration(
                new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                        StupidModBlocks.SULPHUR_ORE.getDefaultState(),
                        32)
        ).range(100).func_242731_b(1);

        noahSulphurFeature = Feature.ORE.withConfiguration(
                new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                        StupidModBlocks.NOAH_SULPHUR_ORE.getDefaultState(),
                        5)
        ).range(16).func_242731_b(3);
    }

    private void biomeSetup(BiomeLoadingEvent event)
    {
        //Overworld..
        if (event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.NONE)
        {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, sulphurFeature).withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, noahSulphurFeature);

            MobSpawnInfoBuilder spawns =  event.getSpawns();
            List<MobSpawnInfo.Spawners> spawners = spawns.getSpawner(EntityClassification.CREATURE);
            for (int i = 0; i < spawners.size();)
            {
                MobSpawnInfo.Spawners spawner = spawners.get(i);

                if (spawner.type == EntityType.COW) spawners.add(new MobSpawnInfo.Spawners(StupidModEntities.POO_COW, spawner.itemWeight, spawner.minCount, spawner.maxCount));
                else if (spawner.type == EntityType.HORSE) spawners.add(new MobSpawnInfo.Spawners(StupidModEntities.POO_HORSE, spawner.itemWeight, spawner.minCount, spawner.maxCount));
                else if (spawner.type == EntityType.MOOSHROOM) spawners.add(new MobSpawnInfo.Spawners(StupidModEntities.POO_MOOSHROOM, spawner.itemWeight, spawner.minCount, spawner.maxCount));
                else if (spawner.type == EntityType.PIG) spawners.add(new MobSpawnInfo.Spawners(StupidModEntities.POO_PIG, spawner.itemWeight, spawner.minCount, spawner.maxCount));
                else if (spawner.type == EntityType.SHEEP) spawners.add(new MobSpawnInfo.Spawners(StupidModEntities.POO_SHEEP, spawner.itemWeight, spawner.minCount, spawner.maxCount));
                else
                {
                    i++;
                    continue;
                }

                spawners.remove(i);
            }
        }
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
            return new ItemStack(StupidModItems.POO);
        }
    };
    
}
