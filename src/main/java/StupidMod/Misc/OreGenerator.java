package StupidMod.Misc;

import StupidMod.StupidMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OreGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) {
            this.generateSulphur(world,random,chunkX * 16,chunkZ * 16);
            this.generateNoahOre(world,random,chunkX * 16,chunkZ * 16);
        }
    }
    
    public void generateSulphur(World world, Random random, int x, int z) {
        for(int k = 0; k < 7; k++){
            int firstBlockXCoord = x + random.nextInt(16);
            int firstBlockYCoord = random.nextInt(48);
            int firstBlockZCoord = z + random.nextInt(16);
            
            (new WorldGenMinable(StupidMod.instance.blocks.blockSulphurOre.getDefaultState(),
                    4 + random.nextInt(12))).generate(world, random, new BlockPos(firstBlockXCoord,firstBlockYCoord,firstBlockZCoord));
        }
    }
    
    public void generateNoahOre(World world,Random random,int x,int z) {
        for(int k = 0; k < 4; k++){
            int firstBlockXCoord = x + random.nextInt(16);
            int firstBlockYCoord = random.nextInt(20);
            int firstBlockZCoord = z + random.nextInt(16);
            
            (new WorldGenMinable(StupidMod.instance.blocks.blockNoahSulphurOre.getDefaultState(),
                    1 + random.nextInt(5))).generate(world, random, new BlockPos(firstBlockXCoord,firstBlockYCoord,firstBlockZCoord));
        }
    }
}
