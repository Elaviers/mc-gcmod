package stupidmod.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import stupidmod.StupidModBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class PooBlock extends Block {
    
    public PooBlock(String name, SoundType sound) {
        super(Properties.create(Material.ORGANIC)
                .sound(sound)
                .hardnessAndResistance(1.0f)
                .tickRandomly());
        
        this.setRegistryName(name);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.getBlock() == StupidModBlocks.FERMENTED_POO) return;
        
        BlockPos checkpos = pos.offset(Direction.UP);
        if (world.getLight(pos) < 10 && world.getBlockState(checkpos).getBlock() instanceof RopeBlock && world.getBlockState(checkpos).get(RopeBlock.WET)){
            world.setBlockState(pos, StupidModBlocks.FERMENTED_POO.getDefaultState());
            return;
        }
    
        for (Direction enumfacing : Direction.Plane.HORIZONTAL)
            if (world.getBlockState(pos.offset(enumfacing).down()).getMaterial() == Material.WATER)
                if (world.getLight(pos) < 5)
                    if (world.rand.nextInt(5) == 0)
                        world.setBlockState(pos, StupidModBlocks.FERMENTED_POO.getDefaultState());
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.SHOVEL;
    }
}
