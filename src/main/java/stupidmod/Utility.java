package stupidmod;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import stupidmod.block.WirelessTorchBlock;
import stupidmod.entity.tile.WirelessTorchTileEntity;

import java.util.List;

public class Utility {
    
    static public WirelessTorchTileEntity setIndividualState(World world, BlockPos pos, boolean state) {
        System.out.println("Setting block at " + pos + " to " + state);

        WirelessTorchTileEntity td = (WirelessTorchTileEntity)world.getTileEntity(pos);
        td.changingState = true;
        
        world.setBlockState(pos, world.getBlockState(pos).with(WirelessTorchBlock.LIT, state));
        
        td.changingState = false;
        return td;
    }
    
    private static final Biome[] overworldBiomes = {
            Biomes.BADLANDS, Biomes.BADLANDS_PLATEAU, Biomes.BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.COLD_OCEAN,
            Biomes.DARK_FOREST, Biomes.DARK_FOREST_HILLS, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN,
            Biomes.DEEP_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.DESERT_LAKES, Biomes.ERODED_BADLANDS,
            Biomes.FLOWER_FOREST, Biomes.FOREST, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS,
            Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.GRAVELLY_MOUNTAINS, Biomes.ICE_SPIKES, Biomes.JUNGLE,
            Biomes.JUNGLE_EDGE, Biomes.JUNGLE_HILLS, Biomes.LUKEWARM_OCEAN, Biomes.MODIFIED_BADLANDS_PLATEAU, Biomes.MODIFIED_GRAVELLY_MOUNTAINS,
            Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MOUNTAIN_EDGE,
            Biomes.MOUNTAINS, Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.OCEAN, Biomes.PLAINS, Biomes.RIVER,
            Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.SNOWY_BEACH,
            Biomes.SNOWY_MOUNTAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.SNOWY_TUNDRA,
            Biomes.STONE_SHORE, Biomes.SUNFLOWER_PLAINS, Biomes.SWAMP, Biomes.SWAMP_HILLS, Biomes.TAIGA, Biomes.TAIGA_HILLS,
            Biomes.TAIGA_MOUNTAINS, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.WARM_OCEAN, Biomes.WOODED_BADLANDS_PLATEAU,
            Biomes.WOODED_HILLS, Biomes.WOODED_MOUNTAINS
    };
    
    private static final Biome[] creatureSpawnBiomes = {
            Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.DARK_FOREST_HILLS, Biomes.FLOWER_FOREST, Biomes.FOREST,
            Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.GRAVELLY_MOUNTAINS,
            Biomes.ICE_SPIKES, Biomes.JUNGLE, Biomes.JUNGLE_EDGE, Biomes.JUNGLE_HILLS, Biomes.LUKEWARM_OCEAN, Biomes.MODIFIED_BADLANDS_PLATEAU,
            Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MOUNTAIN_EDGE,
            Biomes.MOUNTAINS, Biomes.OCEAN, Biomes.SAVANNA_PLATEAU, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.SNOWY_BEACH,
            Biomes.SNOWY_MOUNTAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.STONE_SHORE, Biomes.SWAMP,
            Biomes.SWAMP_HILLS, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.TAIGA_MOUNTAINS, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS,
            Biomes.WOODED_BADLANDS_PLATEAU, Biomes.WOODED_HILLS, Biomes.WOODED_MOUNTAINS
    };
    
    public static void addOverworldOreFeature(GenerationStage.Decoration decoration, ConfiguredFeature feature)
    {
        for (int i = 0; i < overworldBiomes.length; i++)
        {
            overworldBiomes[i].addFeature(decoration, feature);
        }
        
    }
    
    static void removeIfCreature(Biome biome, EntityType<? extends MobEntity> type)
    {
        List<Biome.SpawnListEntry> spawns = biome.getSpawns(EntityClassification.CREATURE);
        
        for (int i = 0; i < spawns.size();)
        {
            if (spawns.get(i).entityType == type)
                spawns.remove(i);
            else
                i++;
        }
    }
    
    public static void removeSpawn(EntityType<? extends MobEntity> type)
    {
        
        for (int i = 0; i < creatureSpawnBiomes.length; i++)
            removeIfCreature(creatureSpawnBiomes[i], type);
    
    }
    
    public static void addOverworldCreatureSpawn(EntityClassification type, Biome.SpawnListEntry entry)
    {
        for (int i = 0; i < creatureSpawnBiomes.length; i++)
            creatureSpawnBiomes[i].getSpawns(EntityClassification.CREATURE).add(entry);
    }
    
}
